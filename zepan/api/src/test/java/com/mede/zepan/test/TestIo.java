package com.mede.zepan.test;

import java.util.ArrayList;
import java.util.List;

import com.mede.zepan.Zepan;
import com.mede.zepan.configs.EncryptionConfig;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.configs.PostConfig;
import com.mede.zepan.configs.RequestConfiguration;
import com.mede.zepan.utils.ZepanUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
/**
 *
 */
public class TestIo {
    
    @Test
    public void testConfigurationJson() throws Exception {
        RequestConfiguration config = new RequestConfiguration();
        config.setChecksum("1234");
        
        config.setImmediateAbort(false);
        config.setInputDirectory("/usr/local/zepan/in");
        
        config.setOutputDirectory("/usr/local/zepan/out");
        
        config.setZip(true);
        config.setZipName("zipper");
        config.setZipStopSize(10000L);
        
        PostConfig postConfig = new PostConfig();
        postConfig.setHost("localhost");
        postConfig.setPassword("password");
        postConfig.setPort(8080);
        postConfig.setProtocol(Zepan.Protocol.HTTP);
        postConfig.setUser("foo");
        

        config.setPostConfig(postConfig);

        EncryptionConfig encryptionConfig = new EncryptionConfig();
        encryptionConfig.setAlg("AES");
        encryptionConfig.setSecret("secret");
        encryptionConfig.setType(EncryptionConfig.SYMMETRIC);
        
        config.setEncryptionConfig(encryptionConfig);

        List<NotificationConfig> list = new ArrayList<>();
        NotificationConfig config1 = new NotificationConfig();
        config1.setType(NotificationConfig.ALL);
        config1.setUser("foo");
        config1.setProtocol(Zepan.Protocol.MAILTO);
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
        config2.setProtocol(Zepan.Protocol.HTTP);
        config2.setHost("localhost");
        config2.setPort(21);
        config2.setPath("none2");
        config2.setPassword("password2");
        list.add(config1);
        
        
        config.setNotificationConfigs(list);
        
        String json = ZepanUtils.getConfigAsJson(config, true);
        System.out.println(json);
        RequestConfiguration other = ZepanUtils.getConfigFromJson(json);
        assertEquals(config, other);
    }

}
