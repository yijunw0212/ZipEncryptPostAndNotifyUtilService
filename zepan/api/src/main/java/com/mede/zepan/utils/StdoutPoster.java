package com.mede.zepan.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.mede.zepan.FileInfo;
import com.mede.zepan.Poster;
import com.mede.zepan.PosterFactory;
import com.mede.zepan.configs.PostConfig;
import com.mede.zepan.exceptions.PostException;

/**
 *
 */
public class StdoutPoster implements Poster {

    protected PostConfig config;
    
    public StdoutPoster(PostConfig config) {
        this.config = config;
    }

    @Override
    public FileInfo post(FileInfo info, InputStream inputStream) throws PostException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            info.setPostStatus(PostConfig.SUCCESS);
            info.setPostURL(config.getUrl());
            return info;
        } catch (IOException e) {
            throw new PostException("Error printing to stdout.", e);
        }

    }

    public static class StdoutPosterFactory implements PosterFactory {

        @Override
        public Poster newPoster(PostConfig config) {
            return new StdoutPoster(config);
        }
    }
}
