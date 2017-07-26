package com.mede.zepan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mede.zepan.FileInfo;
import com.mede.zepan.ProcessInfo;
import com.mede.zepan.SuccessFileInfo;
import com.mede.zepan.SuccessProcessInfo;
import com.mede.zepan.configs.RequestConfiguration;
import com.mede.zepan.exceptions.ZepanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ZepanUtils {
    
    private static Logger log = LoggerFactory.getLogger(com.mede.zepan.utils.ZepanUtils.class);
    
    
    private static ObjectMapper mapper = new ObjectMapper();

    public static String stackTraceToString(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public static File validateInputDir(String inputDir) throws ZepanException {
        Path path = Paths.get(inputDir);
        File f = path.toFile();
        if (!f.exists()) {
            throw new ZepanException("Input directory " + inputDir + " does not exist.");
        }
        if (!f.isDirectory()) {
            throw new ZepanException("Input directory " + inputDir + " is not a directory.");
        }
        if (!f.canRead()) {
            throw new ZepanException("Cannot read from input directory " + inputDir);
        }
        File[] files = f.listFiles((File file) ->!file.getName()
                                                          .startsWith(".") && !file.isDirectory());
        if (files == null || files.length == 0) {
            throw new ZepanException("Input directory " + inputDir + " is empty.");
        }
        return f;
    }

    public static File validateOutputDir(String outputDir) throws ZepanException {
        Path output = Paths.get(outputDir);
        File out = output.toFile();
        if (out.exists()) {
            if (!out.isDirectory()) {
                throw new ZepanException("Output is not a directory " + outputDir);
            }
            if (!out.canWrite()) {
                throw new ZepanException("Cannot write to output  directory " + outputDir);
            }
        } else {
            boolean mk = out.mkdirs();
            if (!mk) {
                throw new ZepanException("Cannot create output directory " + outputDir);
            }
        }
        return out;
    }
    
    public static InputStream getInputStream(String path) throws IOException {
        Path p = Paths.get(path);
        return new FileInputStream(p.toFile());
    }

    public static RequestConfiguration getConfigFromJson(String configuration) throws IOException {
        return mapper.readValue(configuration, RequestConfiguration.class);
    }
    
    public static String getConfigAsJson(RequestConfiguration configuration)
        throws IOException {
         return getConfigAsJson(configuration, false);
    }

    public static String getConfigAsJson(RequestConfiguration configuration, boolean pretty) throws IOException {
        if(pretty) {
            return mapper.writer()
                         .withDefaultPrettyPrinter()
                         .writeValueAsString(configuration);
        } else {
            return mapper.writeValueAsString(configuration);
        }
    }

    public static String getAllInfoAsJson(ProcessInfo info) throws IOException {
        return getAllInfoAsJson(info, false);
    }

    public static String getAllInfoAsJson(ProcessInfo info, boolean pretty) throws IOException {
        if(pretty) {
            return mapper.writer()
                         .withDefaultPrettyPrinter()
                         .writeValueAsString(info);
        } else {
            return mapper.writeValueAsString(info);
        }
    }
    
    public static String getFileName(String path) {
        Path p = Paths.get(path);
        return p.toFile().getName();
    }

    public static String getSuccessInfoAsJson(ProcessInfo info) throws IOException {
       return getSuccessInfoAsJson(info, false); 
    }

    public static String getSuccessInfoAsJson(ProcessInfo info, boolean pretty) throws IOException {
        SuccessProcessInfo processInfo = new SuccessProcessInfo();
        List<FileInfo> infos = info.getFileInfos();
        for (FileInfo fileInfo : infos) {
            if (log.isDebugEnabled()) {
                log.debug("ZepanUtils.getSuccessInfoAsJson: next file info:" + fileInfo);
            }
            if(fileInfo.getError() == null && fileInfo.getPostStatus().equals(FileInfo.SUCCESS)) {
                if (log.isDebugEnabled()) {
                    log.debug("ZepanUtils.getSuccessInfoAsJson file info has no errors. adding to success info.");
                }
                SuccessFileInfo successFileInfo = new SuccessFileInfo();
                successFileInfo.setFileName(getFileName(fileInfo.getOutputPath()));
                successFileInfo.setChecksumType(fileInfo.getChecksumType());
                successFileInfo.setChecksum(fileInfo.getChecksum());
                successFileInfo.setEncryptionAlg(fileInfo.getEncryptionAlg());
                successFileInfo.setEncrypted(fileInfo.getEncrypted());
                successFileInfo.setEncryptionType(fileInfo.getEncryptionType());
                successFileInfo.setPostUrl(fileInfo.getPostURL());
                successFileInfo.setZipped(fileInfo.getZipped());
                processInfo.addFileInfo(successFileInfo);
            }
        }
        if(processInfo.getFileInfos().size() > 0) {
            if (pretty) {
                return mapper.writer()
                             .withDefaultPrettyPrinter()
                             .writeValueAsString(processInfo);
            } else {
                return mapper.writeValueAsString(processInfo);
            }
        }
        return null;
    }

    public static ProcessInfo getAllInfoFromJson(String info) throws IOException {
        return mapper.readValue(info, ProcessInfo.class);
    }

    public static SuccessProcessInfo getSuccessInfoFromJson(String info) throws IOException {
        return mapper.readValue(info, SuccessProcessInfo.class);
    }


    public static Collection<Collection<File>> groupFiles(int stopSize, File[] files) throws ZepanException {
        int currentSize = 0;
        Collection<Collection<File>> allFileLists = new ArrayList<>();
        Collection<File> tempFileList = new ArrayList<>();
        if(files != null) {
            for (File toAssign : files) {
                if (currentSize >= stopSize) {
                    allFileLists.add(tempFileList);
                    tempFileList = new ArrayList<>();
                    currentSize = 0;
                }
                currentSize++;
                tempFileList.add(toAssign);
            }
            if (tempFileList.size() > 0) {
                allFileLists.add(tempFileList);
            }
        }
        return allFileLists;
    }

    public static Collection<File> getFiles(File[] files)
        throws ZepanException {
        
        Collection<File> allFileLists = new ArrayList<>();
        if (files != null) {
            allFileLists.addAll(Arrays.asList(files));
        }
        return allFileLists;
    }
}
