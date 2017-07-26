package com.mede.zepan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.configs.PostConfig;
import com.mede.zepan.configs.RequestConfiguration;
import com.mede.zepan.decorator.ChecksumDecorator;
import com.mede.zepan.encryption.AesCbcEncrypter;
import com.mede.zepan.encryption.AesEncrypter;
import com.mede.zepan.exceptions.EncryptException;
import com.mede.zepan.exceptions.NotifyException;
import com.mede.zepan.exceptions.PostException;
import com.mede.zepan.exceptions.ZepanException;
import com.mede.zepan.processor.FileProcessor;
import com.mede.zepan.processor.ZipFileProcessor;
import com.mede.zepan.utils.ZepanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point into Zepan.
 */
public class Zepan {

    private static Logger log = LoggerFactory.getLogger(com.mede.zepan.Zepan.class);

    public enum Protocol {
        HTTP, FTP, MAILTO, LOG, STDOUT
    }

    private Map<Protocol, PosterFactory> posters = new HashMap<>();
    private Map<Protocol, List<NotifierFactory>> notifiers = new HashMap<>();
    private Map<String, EncrypterFactory> encrypters = new HashMap<>();

    public Zepan() {
        registerEncrypter(AesCbcEncrypter.ALGORITHM, new AesCbcEncrypter.AesCbcEncrypterFactory());
        registerEncrypter(AesEncrypter.ALGORITHM, new AesEncrypter.AesEncrypterFactory());
    }

    public void registerPoster(Protocol protocol, PosterFactory factory) {
        PosterFactory existing = posters.put(protocol, factory);
        if (existing != null) {
            log.warn("Over written poster factory for protocol " + protocol);
        }
    }

    public void registerNotifier(Protocol protocol, NotifierFactory factory) {
        List<NotifierFactory> factories = notifiers.get(protocol);
        if (factories == null) {
            factories = new ArrayList<>();
        }
        factories.add(factory);
        notifiers.put(protocol, factories);
    }

    public void registerEncrypter(String alg, EncrypterFactory factory) {
        EncrypterFactory existing = encrypters.put(alg, factory);
        if (existing != null) {
            log.warn("Over written encrypter factory for alg " + alg);
        }
    }

    public ProcessInfo process(RequestConfiguration configuration) throws ZepanException {
        File f = ZepanUtils.validateInputDir(configuration.getInputDirectory());
        File[] files = f.listFiles((File file) -> !file.getName()
                                                       .startsWith(".") && !file.isDirectory());
        int stopSize = configuration.getThreadingFileGroupSize() == null ?
            Integer.MAX_VALUE : configuration.getThreadingFileGroupSize();

        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setRequestConfiguration(configuration);
        try {
            if (configuration.getThreading() != null && configuration.getThreading()) {

                Collection<Collection<File>> groupedFiles = ZepanUtils.groupFiles(stopSize, files);
                ExecutorService executor = Executors.newFixedThreadPool(groupedFiles.size());
                List<Future<List<FileInfo>>> futures = new ArrayList<>();
                for (Collection<File> fileList : groupedFiles) {
                    ThreadProcess threadProcess = new ThreadProcess(fileList, configuration);
                    futures.add(executor.submit(threadProcess));
                }
                for (Future<List<FileInfo>> future : futures) {
                    processInfo.addFileInfos(future.get());
                }
                boolean done = executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                if (!done) {
                    List<Runnable> threads = executor.shutdownNow();
                    if (threads.size() > 0) {
                        log.warn("Unfinished threads found when shutting down executor:" + threads);
                        throw new ZepanException(
                            "Unfinished threads found when shutting down executor:" + threads);
                    }
                }
            } else {
                Collection<File> groupedFiles = ZepanUtils.getFiles(files);
                ThreadProcess threadProcess = new ThreadProcess(groupedFiles, configuration);
                processInfo.addFileInfos(threadProcess.call());
            }
            processInfo = processNotify(processInfo, configuration);
            return processInfo;
        } catch (InterruptedException e) {
            Thread.currentThread()
                  .interrupt();
            throw new ZepanException(e);
        } catch (Exception e) {
            if (e instanceof ZepanException) {
                throw (ZepanException) e;
            }
            throw new ZepanException(e);
        }

    }

    private ProcessInfo processNotify(ProcessInfo processInfo, RequestConfiguration configuration)
        throws ZepanException {
        if (log.isDebugEnabled()) {
            log.debug("ENTER: Zepan.processNotify: "
                + "processInfo = ["
                + processInfo
                + "], configuration = ["
                + configuration
                + "]");
        }

        List<NotificationConfig> notificationConfigs = configuration.getNotificationConfigs();
        if (notificationConfigs.size() == 0) {
            if (log.isInfoEnabled()) {
                log.info("Zepan.processNotify: no notifiers in config");
            }
        }
        for (NotificationConfig notificationConfig : notificationConfigs) {
            Zepan.Protocol protocol = notificationConfig.getProtocol();
            if (protocol == null) {
                throw new PostException("null protocol");
            }
            List<NotifierFactory> factories = notifiers.get(protocol);
            if (factories != null) {
                for (NotifierFactory factory : factories) {
                    Notifier notifier = factory.newNotifier(notificationConfig);
                    try {
                        String configAsString;
                        if (notificationConfig.getType()
                                              .equals(NotificationConfig.SUCCESS)) {
                            configAsString = ZepanUtils.getSuccessInfoAsJson(processInfo, true);
                        } else {
                            configAsString = ZepanUtils.getAllInfoAsJson(processInfo, true);
                        }
                        notifier.notify(notificationConfig.getType(), configAsString);
                    } catch (NotifyException e) {
                        if (configuration.getImmediateAbort()) {
                            throw e;
                        }
                    } catch (Exception e) {
                        throw new ZepanException(
                            "Error instantiating notifier with factory " + factory, e);
                    }
                }
            } else {
                throw new ZepanException(
                    "Not notifier factory for protocol " + protocol + " registered.");
            }
        }
        return processInfo;
    }

    private class ThreadProcess implements Callable<List<FileInfo>> {
        private Collection<File> files;
        private RequestConfiguration configuration;

        public ThreadProcess(Collection<File> fileToProcess, RequestConfiguration configuration) {
            this.files = fileToProcess;
            this.configuration = configuration;
        }

        @Override
        public List<FileInfo> call() throws Exception {
            Collection<FileInfo> infos = processIO(configuration);
            return processPost(configuration, infos);

        }

        private List<FileInfo> processPost(RequestConfiguration configuration,
            Collection<FileInfo> infos) throws ZepanException {
            if (log.isDebugEnabled()) {
                log.debug("ENTER: Zepan.processPost: "
                    + "configuration = ["
                    + configuration
                    + "], infos = ["
                    + infos
                    + "]");
            }

            ArrayList<FileInfo> posted = new ArrayList<>();
            PostConfig postConfig = configuration.getPostConfig();
            if (postConfig != null) {
                Zepan.Protocol protocol = postConfig.getProtocol();
                if (protocol == null) {
                    throw new PostException("null protocol");
                }
                PosterFactory factory = posters.get(protocol);
                if (factory != null) {
                    Poster poster = factory.newPoster(postConfig);
                    for (FileInfo info : infos) {
                        InputStream stream = null;
                        try {
                            stream = ZepanUtils.getInputStream(info.getOutputPath());
                        } catch (IOException e) {
                            throw new ZepanException("Error getting stream for info output path "
                                + info.getOutputPath());
                        }
                        try {
                            posted.add(poster.post(info, stream));
                        } catch (PostException e) {
                            if (configuration.getImmediateAbort()) {
                                throw e;
                            }
                        }
                    }
                } else {
                    throw new ZepanException(
                        "Not poster factory for protocol " + protocol + " registered.");
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("Zepan.processPost: no posters registered.");
                }
            }
            return posted;
        }

        private Collection<FileInfo> processIO(RequestConfiguration config) throws ZepanException {
            if (log.isDebugEnabled()) {
                log.debug("ENTER: Zepan.processIO: " + "config = [" + config + "]");
            }

            List<FileInfo> processedFileInfos = new ArrayList<>();

            //            File f = ZepanUtils.validateInputDir(config.getInputDirectory());
            File out = ZepanUtils.validateOutputDir(config.getOutputDirectory());
            int zipCount = 0;
            IOProcessor ioProcessor = null;
            ChecksumDecorator checksumDecorator = null;
            //            File[] files = f.listFiles((File file) -> !file.getName()
            //                    .startsWith(".") && !file.isDirectory());
            File[] filesToProcess = files.toArray(new File[files.size()]);
            for (int i = 0; i < filesToProcess.length; i++) { // we know it's not null by now
                File file = filesToProcess[i];
                if (log.isDebugEnabled()) {
                    log.debug("Zepan.processIO next file " + file.getAbsolutePath());
                }
                FileInfo info = new FileInfo();
                info.setInputPath(file.getAbsolutePath());
                try {
                    if (ioProcessor == null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Zepan.processIO: io processor is null. Creating a new one.");
                        }
                        if (config.getZip()) {
                            ioProcessor = new ZipFileProcessor(
                                new File(out, config.getZipName() + "-" + (zipCount++) + ".zip"),
                                config.getZipStopSize());
                        } else {
                            ioProcessor = new FileProcessor(new File(out, file.getName()));
                        }
                        if (config.getChecksum() != null) {
                            checksumDecorator = new ChecksumDecorator(config.getChecksum());
                            ioProcessor.accept(checksumDecorator);
                        }
                    }
                    if (config.getEncryptionConfig() != null) {
                        String alg = config.getEncryptionConfig()
                                           .getAlg();
                        EncrypterFactory factory = encrypters.get(alg);
                        if (factory == null) {
                            throw new EncryptException(
                                "Not encrypter factory registered for alg " + alg);
                        }
                        ioProcessor.accept(
                            factory.newEncrypter(config.getEncryptionConfig(), info));
                    }
                    InputStream inputStream = new FileInputStream(file);
                    ioProcessor.process(info, inputStream);

                    if (i == filesToProcess.length - 1 && !ioProcessor.isClosed()) {
                        ioProcessor.close();
                    }
                    if (ioProcessor.isClosed()) {
                        FileInfo received = ioProcessor.getFileInfo();
                        if (checksumDecorator != null) {
                            received.setChecksum(checksumDecorator.getChecksum());
                            received.setChecksumType(checksumDecorator.getDigestType());
                        }
                        processedFileInfos.add(received);
                        ioProcessor = null;
                    }
                } catch (Exception e) {
                    ZepanException ex;
                    if (e instanceof ZepanException) {
                        ex = (ZepanException) e;
                    } else {
                        ex = new ZepanException(e);
                    }
                    FileInfo curr = info;
                    if (ioProcessor != null) {
                        FileInfo processorInfo = ioProcessor.getFileInfo();
                        if (processorInfo != null) {
                            curr = processorInfo;
                        }
                    }
                    curr.setError(ex.getName());
                    curr.setErrorDescription(ZepanUtils.stackTraceToString(ex));
                    if (config.getImmediateAbort()) {
                        throw ex;
                    }
                    processedFileInfos.add(curr);
                }
            }
            return processedFileInfos;
        }
    }
}
