package com.mede.zepan.http;

import java.io.IOException;
import java.io.InputStream;

import com.mede.zepan.Poster;
import com.mede.zepan.PosterFactory;
import com.mede.zepan.configs.PostConfig;
import com.mede.zepan.exceptions.PostException;
import com.mede.zepan.FileInfo;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 */
public class HttpPoster implements Poster {

    private PostConfig config;

    public HttpPoster(PostConfig config) {
        this.config = config;
    }

    @Override
    public FileInfo post(FileInfo info, InputStream inputStream) throws PostException {
        final String postTo = config.getUrl();
        HttpPost httpPost = new HttpPost(postTo);
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(config.getUser(),
            config.getPassword());

        info.setPostURL(postTo);

        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        try {
            Header header = new BasicScheme().authenticate(creds, httpPost, null);
            httpPost.addHeader("Authorization", header.getValue());
            httpclient = HttpClients.createDefault();

            HttpEntity reqEntity = new InputStreamEntity(inputStream);
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
            info.setPostStatus("SUCCESS");
            return info;
        } catch (Exception ex) {
            throw new PostException(ex);
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
    
    public static class HttpPosterFactory implements PosterFactory {

        @Override
        public Poster newPoster(PostConfig config) {
            return new HttpPoster(config);
        }
    }

}
