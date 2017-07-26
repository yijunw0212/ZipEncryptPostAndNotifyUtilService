package com.mede.zepan.http;

import java.io.IOException;

import com.mede.zepan.Notifier;
import com.mede.zepan.NotifierFactory;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.exceptions.NotifyException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by JWang on 6/26/2017.
 */
public class HttpNotifier implements Notifier {


    protected NotificationConfig config;

    public HttpNotifier(NotificationConfig config) {
        this.config = config;
    }

    @Override
    public void notify(String type, String processInfo) throws NotifyException {
        final String notifyTo = config.getPath();
        HttpPost httpPost = new HttpPost(notifyTo);
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(config.getUser(),
                config.getPassword());

        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        try {
            Header header = new BasicScheme().authenticate(creds, httpPost, null);
            httpPost.addHeader("Authorization", header.getValue());
            httpclient = HttpClients.createDefault();

            HttpEntity reqEntity = new StringEntity("Received notification of type " + type + ": " + processInfo);
            httpPost.setEntity(reqEntity);
            response = httpclient.execute(httpPost);
            StatusLine line = response.getStatusLine();
            log.info(line.toString());
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                log.info("Content Length：" + resEntity.getContentLength());
                EntityUtils.consume(resEntity);
            }
            int status = line.getStatusCode();
            if(status < 200 || status >= 300) {
                throw new IOException("Unexpected response code:" + status);
            }
        } catch (Exception ex) {
            throw new NotifyException(ex);
        } finally {
            try {
                if(response != null) {
                    response.close();
                }
                if(httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                log.warn("Error closing client", e);
            }
        }
    }


    public static class HttpNotifierFactory implements NotifierFactory {

        @Override
        public Notifier newNotifier(NotificationConfig config) {
            return new HttpNotifier(config);
        }
    }
}
