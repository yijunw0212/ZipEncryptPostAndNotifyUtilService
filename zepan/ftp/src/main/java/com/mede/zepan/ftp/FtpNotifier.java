package com.mede.zepan.ftp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mede.zepan.Notifier;
import com.mede.zepan.NotifierFactory;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.exceptions.NotifyException;

/**
 * Created by JWang on 6/26/2017.
 */
    public class FtpNotifier implements Notifier {

    protected NotificationConfig config;

    public FtpNotifier(NotificationConfig config) {
        this.config = config;
    }

    @Override
    public void notify(String type, String processInfo) throws NotifyException {
        final String notifyTo = config.getPath();
        String SFTPHOST = config.getHost();
        int SFTPPORT = config.getPort();
        String SFTPUSER = config.getUser();
        String SFTPPASS = config.getPassword();

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        InputStream stream = new ByteArrayInputStream((type + ": " + processInfo).getBytes(StandardCharsets.UTF_8));
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties propertiesConfig = new java.util.Properties();
            propertiesConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(propertiesConfig);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(notifyTo);
            channelSftp.put(stream,"ZepanProcessInfo");
        } catch (Exception ex) {
            throw new NotifyException(ex);
        }
        finally{
            if(channelSftp != null) {
                channelSftp.exit();
            }
            if(channel != null) {
                channel.disconnect();
            }
            if(session != null) {
                session.disconnect();
            }
        }
    }


    public static class FtpNotifierFactory implements NotifierFactory {

        @Override
        public Notifier newNotifier(NotificationConfig config) {
            return new FtpNotifier(config);
        }
    }
}
