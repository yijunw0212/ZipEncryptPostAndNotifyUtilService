package com.mede.zepan.application;

import java.util.List;

import com.mede.zepan.Zepan;
import com.mede.zepan.configs.EncryptionConfig;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.configs.PostConfig;
import com.mede.zepan.configs.RequestConfiguration;
import com.mede.zepan.ftp.FtpNotifier;
import com.mede.zepan.ftp.FtpPoster;
import com.mede.zepan.http.HttpNotifier;
import com.mede.zepan.http.HttpPoster;
import com.mede.zepan.mailto.MailtoNotifier;
import com.mede.zepan.utils.LogNotifier;
import com.mede.zepan.utils.StdoutNotifier;
import com.mede.zepan.utils.StdoutPoster;

/**
 *
 */
public class Main {

    public static void main(String[] args) {

        Zepan zepan = new Zepan();
        

    }
    
//    public static void autoRegister(Zepan zepan, RequestConfiguration configuration) {
//        EncryptionConfig encryptionConfig = configuration.getEncryptionConfig();
//        PostConfig postConfig = configuration.getPostConfig();
//        List<NotificationConfig> notificationConfigs = configuration.getNotificationConfigs();
//        if (postConfig != null) {
//            Zepan.Protocol configProtocol = postConfig.getProtocol();
//
//            switch (configProtocol) {
//                case STDOUT:
//                    zepan.registerPoster(configProtocol, new StdoutPoster.StdoutPosterFactory());
//                case HTTP:
//                    zepan.registerPoster(configProtocol, new HttpPoster.HttpPosterFactory());
//                    //                case MAILTO: registerPoster(configProtocol,new MailtoPoster.MailtoPosterFactory());
//                case FTP:
//                    zepan.registerPoster(configProtocol, new FtpPoster.FtpPosterFactory());
//                    //                case LOG: registerPoster(configProtocol,new LogPoster.LogPosterFactory());
//            }
//        }
//
//        if (notificationConfigs != null && notificationConfigs.size() > 0) {
//            for (NotificationConfig notificationConfig : notificationConfigs) {
//                Zepan.Protocol configProtocol = notificationConfig.getProtocol();
//                switch (notificationConfig.getProtocol()) {
//                    case STDOUT:
//                        zepan.registerNotifier(configProtocol, new StdoutNotifier.StdoutNotifierFactory());
//                    case HTTP:
//                        zepan.registerNotifier(configProtocol, new HttpNotifier.HttpNotifierFactory());
//                    case MAILTO:
//                        zepan.registerNotifier(configProtocol, new MailtoNotifier.MailtoNotifierFactory());
//                    case FTP:
//                        zepan.registerNotifier(configProtocol, new FtpNotifier.FtpNotifierFactory());
//                    case LOG:
//                        zepan.registerNotifier(configProtocol, new LogNotifier.LogNotifierFactory());
//                }
//            }
//        }
//    }
}
