package com.mede.zepan.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.mede.zepan.Zepan;
import com.mede.zepan.configs.EncryptionConfig;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.configs.PostConfig;
import com.mede.zepan.configs.RequestConfiguration;
import com.mede.zepan.utils.StdoutNotifier;
import com.mede.zepan.utils.StdoutPoster;
import com.mede.zepan.SuccessFileInfo;
import com.mede.zepan.SuccessProcessInfo;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 *
 */
public class TestWorkflow {

    /**
     * test encryption and zip. One zip should contain two files. 
     * @throws Exception
     */
    @Test
    public void testSuccessNotificationNoChecksum() throws Exception {

        Zepan zepan = new Zepan();
        zepan.registerPoster(Zepan.Protocol.STDOUT, new StdoutPoster.StdoutPosterFactory());
        
        RequestConfiguration config = new RequestConfiguration();
        
        URL url = Thread.currentThread().getContextClassLoader().getResource("input/file1.txt");
        File f = new File(url.toURI());
        
        
        config.setInputDirectory(f.getParent());
        File output = new File(f.getParentFile(), "output");
        File[] children = output.listFiles();
        if(children != null) {
            for (File child : children) {
                child.delete();
            }
        }
        config.setOutputDirectory(output.getAbsolutePath());

        config.setZip(true);
        config.setZipName("zipper");
        config.setZipStopSize(10000L);
        config.setImmediateAbort(true);

        PostConfig postConfig = new PostConfig();
        postConfig.setHost("localhost");
        postConfig.setPassword("password");
        postConfig.setPort(8080);
        postConfig.setProtocol(Zepan.Protocol.STDOUT);
        postConfig.setUser("foo");
        postConfig.setUrl("stdout://localhost");


        config.setPostConfig(postConfig);

        EncryptionConfig encryptionConfig = new EncryptionConfig();
        encryptionConfig.setAlg("AES/CBC/PKCS5Padding");
        encryptionConfig.setSecret("secret");
        encryptionConfig.setSalt("foobar");
        encryptionConfig.setType(EncryptionConfig.SYMMETRIC);

        config.setEncryptionConfig(encryptionConfig);

        List<NotificationConfig> list = new ArrayList<>();
        NotificationConfig config1 = new NotificationConfig();
        config1.setType(NotificationConfig.ALL);
        config1.setUser("foo");
        config1.setProtocol(Zepan.Protocol.STDOUT);
        config1.setMailFrom("foo@foobar.com");
        config1.setMailtoHost("smtp.other.com");
        config1.setHost("foobar");
        config1.setPort(21);
        config1.setPath("none");
        config1.setPassword("password1");
        list.add(config1);

        NotificationConfig config2 = new NotificationConfig();
        config2.setType(NotificationConfig.SUCCESS);
        config2.setUser("foo");
        config2.setProtocol(Zepan.Protocol.STDOUT);
        config2.setMailFrom("foo@foobar.com");
        config2.setMailtoHost("smtp.other.com");
        config2.setHost("foobar");
        config2.setPort(21);
        config2.setPath("none");
        config2.setPassword("password1");
        list.add(config2);

        config.setNotificationConfigs(list);
        
        SuccessProcessInfo expected = new SuccessProcessInfo();
        SuccessFileInfo fileInfo = new SuccessFileInfo();
        fileInfo.setEncryptionType("SYMMETRIC");
        fileInfo.setEncrypted(true);
        fileInfo.setEncryptionAlg("AES/CBC/PKCS5Padding");
        fileInfo.setPostUrl("stdout://localhost");
        fileInfo.setFileName("zipper-0.zip");
        fileInfo.setZipped(true);
        expected.addFileInfo(fileInfo);


        zepan.registerNotifier(Zepan.Protocol.STDOUT, new TestNotifier.TestNotifierFactory(expected));
        zepan.process(config);
        ZipFile zipFile = new ZipFile(new File(output, "zipper-0.zip"));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        int count = 0;
        while (entries.hasMoreElements()) {
            count++;
            ZipEntry zipEntry = entries.nextElement();
            if (count == 1) {
                assertEquals(zipEntry.getName(), "file1.txt");
            } else if (count == 2) {
                assertEquals(zipEntry.getName(), "file2.txt");
            }
        }
        assertEquals(count, 2);

    }

    /**
     * test setZipStopSize results in two zip files containing one file each.
     * @throws Exception
     */
    @Test
    public void testMultipleZips() throws Exception {

        Zepan zepan = new Zepan();
        zepan.registerPoster(Zepan.Protocol.STDOUT, new StdoutPoster.StdoutPosterFactory());
        zepan.registerNotifier(Zepan.Protocol.STDOUT, new StdoutNotifier.StdoutNotifierFactory());

        RequestConfiguration config = new RequestConfiguration();
        config.setChecksum("SHA1");
        

        URL url = Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("input/file1.txt");
        File f = new File(url.toURI());


        config.setInputDirectory(f.getParent());
        File output = new File(f.getParentFile(), "output1");
        File[] children = output.listFiles();
        if (children != null) {
            for (File child : children) {
                child.delete();
            }
        }
        config.setOutputDirectory(output.getAbsolutePath());

        config.setZip(true);
        config.setZipName("zipper");
        config.setZipStopSize(1000L);
        config.setImmediateAbort(true);

        PostConfig postConfig = new PostConfig();
        postConfig.setHost("localhost");
        postConfig.setPassword("password");
        postConfig.setPort(8080);
        postConfig.setProtocol(Zepan.Protocol.STDOUT);
        postConfig.setUser("foo");
        postConfig.setUrl("stdout://localhost");


        config.setPostConfig(postConfig);

        EncryptionConfig encryptionConfig = new EncryptionConfig();
        encryptionConfig.setAlg("AES/CBC/PKCS5Padding");
        encryptionConfig.setSecret("secret");
        encryptionConfig.setSalt("foobar");
        encryptionConfig.setType(EncryptionConfig.SYMMETRIC);

        config.setEncryptionConfig(encryptionConfig);

        List<NotificationConfig> list = new ArrayList<>();
        NotificationConfig config1 = new NotificationConfig();
        config1.setType(NotificationConfig.ALL);
        config1.setUser("foo");
        config1.setProtocol(Zepan.Protocol.STDOUT);
        config1.setMailFrom("foo@foobar.com");
        config1.setMailtoHost("smtp.other.com");
        config1.setHost("foobar");
        config1.setPort(21);
        config1.setPath("none");
        config1.setPassword("password1");
        list.add(config1);

        NotificationConfig config2 = new NotificationConfig();
        config2.setType(NotificationConfig.SUCCESS);
        config2.setUser("foo");
        config2.setProtocol(Zepan.Protocol.STDOUT);
        config2.setMailFrom("foo@foobar.com");
        config2.setMailtoHost("smtp.other.com");
        config2.setHost("foobar");
        config2.setPort(21);
        config2.setPath("none");
        config2.setPassword("password1");
        list.add(config2);

        config.setNotificationConfigs(list);

        zepan.process(config);
        
        String[] zips = output.list((dir, name) -> name.endsWith(".zip"));
        assertNotNull(zips);
        assertEquals(zips.length, 2);
        ZipFile zipFile = new ZipFile(new File(output, "zipper-0.zip"));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        int count = 0;
        while (entries.hasMoreElements()) {
            count++;
            ZipEntry zipEntry = entries.nextElement();
            if (count == 1) {
                assertEquals(zipEntry.getName(), "file1.txt");
            } 
        }
        assertEquals(count, 1);
        count = 0;
        zipFile = new ZipFile(new File(output, "zipper-1.zip"));
        entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            count++;
            ZipEntry zipEntry = entries.nextElement();
            if (count == 1) {
                assertEquals(zipEntry.getName(), "file2.txt");
            }
        }
        assertEquals(count, 1);
        
    }

    /**
     * tests no zipping and no salt with 16 byte AES key, meaning checksums
     * are deterministic and two text files will be in output2/
     * @throws Exception
     */
    @Test
    public void testNoZipConsistentChecksum() throws Exception {

        Zepan zepan = new Zepan();
        zepan.registerPoster(Zepan.Protocol.STDOUT, new StdoutPoster.StdoutPosterFactory());

        RequestConfiguration config = new RequestConfiguration();
        config.setChecksum("SHA1");
        
        URL url = Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("input/file1.txt");
        File f = new File(url.toURI());


        config.setInputDirectory(f.getParent());
        File output = new File(f.getParentFile(), "output2");
        File[] children = output.listFiles();
        if (children != null) {
            for (File child : children) {
                child.delete();
            }
        }
        config.setOutputDirectory(output.getAbsolutePath());
        
        config.setImmediateAbort(true);

        PostConfig postConfig = new PostConfig();
        postConfig.setHost("localhost");
        postConfig.setPassword("password");
        postConfig.setPort(8080);
        postConfig.setProtocol(Zepan.Protocol.STDOUT);
        postConfig.setUser("foo");
        postConfig.setUrl("stdout://localhost");


        config.setPostConfig(postConfig);

        EncryptionConfig encryptionConfig = new EncryptionConfig();
        encryptionConfig.setAlg("AES");
        encryptionConfig.setSecret("*iamsixteenlong*");
        encryptionConfig.setType(EncryptionConfig.SYMMETRIC);

        config.setEncryptionConfig(encryptionConfig);

        List<NotificationConfig> list = new ArrayList<>();
        NotificationConfig config1 = new NotificationConfig();
        config1.setType(NotificationConfig.ALL);
        config1.setUser("foo");
        config1.setProtocol(Zepan.Protocol.STDOUT);
        config1.setMailFrom("foo@foobar.com");
        config1.setMailtoHost("smtp.other.com");
        config1.setHost("foobar");
        config1.setPort(21);
        config1.setPath("none");
        config1.setPassword("password1");
        list.add(config1);

        NotificationConfig config2 = new NotificationConfig();
        config2.setType(NotificationConfig.SUCCESS);
        config2.setUser("foo");
        config2.setProtocol(Zepan.Protocol.STDOUT);
        config2.setMailFrom("foo@foobar.com");
        config2.setMailtoHost("smtp.other.com");
        config2.setHost("foobar");
        config2.setPort(21);
        config2.setPath("none");
        config2.setPassword("password1");
        list.add(config2);

        config.setNotificationConfigs(list);

        SuccessProcessInfo expected = new SuccessProcessInfo();
        SuccessFileInfo fileInfo = new SuccessFileInfo();
        fileInfo.setEncryptionType("SYMMETRIC");
        fileInfo.setEncrypted(true);
        fileInfo.setEncryptionAlg("AES");
        fileInfo.setPostUrl("stdout://localhost");
        fileInfo.setFileName("file1.txt");
        fileInfo.setZipped(false);
        fileInfo.setChecksumType("SHA1");
        fileInfo.setChecksum("e316543fa812d50aea1da31f3c7d1092fdd3cc0a");
        expected.addFileInfo(fileInfo);

        SuccessFileInfo fileInfo2 = new SuccessFileInfo();
        fileInfo2.setEncryptionType("SYMMETRIC");
        fileInfo2.setEncrypted(true);
        fileInfo2.setEncryptionAlg("AES");
        fileInfo2.setPostUrl("stdout://localhost");
        fileInfo2.setFileName("file2.txt");
        fileInfo2.setZipped(false);
        fileInfo2.setChecksumType("SHA1");
        fileInfo2.setChecksum("e316543fa812d50aea1da31f3c7d1092fdd3cc0a");
        expected.addFileInfo(fileInfo2);
        
        zepan.registerNotifier(Zepan.Protocol.STDOUT, new TestNotifier.TestNotifierFactory(expected));

        zepan.process(config);

        String[] files = output.list((dir, name) -> name.endsWith(".txt"));
        assertNotNull(files);
        assertEquals(files.length, 2);
        
    }

    /**
     * tests no zipping and no encryption, meaning checksums
     * are deterministic and two text files will be in output3/
     *
     * @throws Exception
     */
    @Test
    public void testNoZipNoEncryptionConsistentChecksum() throws Exception {

        Zepan zepan = new Zepan();
        zepan.registerPoster(Zepan.Protocol.STDOUT, new StdoutPoster.StdoutPosterFactory());

        RequestConfiguration config = new RequestConfiguration();
        config.setChecksum("SHA1");

        URL url = Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("input/file1.txt");
        File f = new File(url.toURI());


        config.setInputDirectory(f.getParent());
        File output = new File(f.getParentFile(), "output3");
        File[] children = output.listFiles();
        if (children != null) {
            for (File child : children) {
                child.delete();
            }
        }
        config.setOutputDirectory(output.getAbsolutePath());

        config.setImmediateAbort(true);

        PostConfig postConfig = new PostConfig();
        postConfig.setHost("localhost");
        postConfig.setPassword("password");
        postConfig.setPort(8080);
        postConfig.setProtocol(Zepan.Protocol.STDOUT);
        postConfig.setUser("foo");
        postConfig.setUrl("stdout://localhost");


        config.setPostConfig(postConfig);

        List<NotificationConfig> list = new ArrayList<>();
        NotificationConfig config1 = new NotificationConfig();
        config1.setType(NotificationConfig.ALL);
        config1.setUser("foo");
        config1.setProtocol(Zepan.Protocol.STDOUT);
        config1.setMailFrom("foo@foobar.com");
        config1.setMailtoHost("smtp.other.com");
        config1.setHost("foobar");
        config1.setPort(21);
        config1.setPath("none");
        config1.setPassword("password1");
        list.add(config1);

        NotificationConfig config2 = new NotificationConfig();
        config2.setType(NotificationConfig.SUCCESS);
        config2.setUser("foo");
        config2.setProtocol(Zepan.Protocol.STDOUT);
        config2.setMailFrom("foo@foobar.com");
        config2.setMailtoHost("smtp.other.com");
        config2.setHost("foobar");
        config2.setPort(21);
        config2.setPath("none");
        config2.setPassword("password1");
        list.add(config2);

        config.setNotificationConfigs(list);

        SuccessProcessInfo expected = new SuccessProcessInfo();
        SuccessFileInfo fileInfo = new SuccessFileInfo();
        fileInfo.setEncrypted(false);
        fileInfo.setPostUrl("stdout://localhost");
        fileInfo.setFileName("file1.txt");
        fileInfo.setZipped(false);
        fileInfo.setChecksumType("SHA1");
        fileInfo.setChecksum("74da314f0f404721b497148bc12f961b1288ffc8");
        expected.addFileInfo(fileInfo);

        SuccessFileInfo fileInfo2 = new SuccessFileInfo();
        fileInfo2.setEncrypted(false);
        fileInfo2.setPostUrl("stdout://localhost");
        fileInfo2.setFileName("file2.txt");
        fileInfo2.setZipped(false);
        fileInfo2.setChecksumType("SHA1");
        fileInfo2.setChecksum("74da314f0f404721b497148bc12f961b1288ffc8");
        expected.addFileInfo(fileInfo2);

        zepan.registerNotifier(Zepan.Protocol.STDOUT, new TestNotifier.TestNotifierFactory(expected));

        zepan.process(config);

        String[] files = output.list((dir, name) -> name.endsWith(".txt"));
        assertNotNull(files);
        assertEquals(files.length, 2);
        assertEquals(new File(output, "file1.txt").length(), new File(f.getParent(), "file1.txt").length());
        assertEquals(new File(output, "file2.txt").length(), new File(f.getParent(), "file2.txt").length());

    }

}
