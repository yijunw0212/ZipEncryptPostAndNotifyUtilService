package com.mede.zepan.ftp;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mede.zepan.Poster;
import com.mede.zepan.PosterFactory;
import com.mede.zepan.configs.PostConfig;
import com.mede.zepan.exceptions.PostException;
import com.mede.zepan.FileInfo;

/**
 * Created by JWang on 6/28/2017.
 */
public class FtpPoster implements Poster{
    private PostConfig config;

    public FtpPoster(PostConfig config) {
        this.config = config;
    }

    @Override
    public FileInfo post(FileInfo info, InputStream inputStream) throws PostException {
        final String postTo = config.getUrl();
        String SFTPHOST = config.getHost();
        int SFTPPORT = config.getPort();
        String SFTPUSER = config.getUser();
        String SFTPPASS = config.getPassword();

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        info.setPostURL(postTo);
        info.setPostStatus("SUCCESS");
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
            channelSftp.cd(postTo);
            channelSftp.put(inputStream, inputStream.toString());
            info.setPostStatus("SUCCESS");
            return info;
        } catch (Exception ex) {
            throw new PostException(ex);
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

    public static class FtpPosterFactory implements PosterFactory {

        @Override
        public Poster newPoster(PostConfig config) {
            return new FtpPoster(config);
        }
    }
}
