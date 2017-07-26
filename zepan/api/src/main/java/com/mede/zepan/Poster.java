package com.mede.zepan;

import java.io.InputStream;

import com.mede.zepan.exceptions.PostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public interface Poster {

    Logger log = LoggerFactory.getLogger(Poster.class);
    
    FileInfo post(FileInfo info, InputStream inputStream) throws PostException;
    
}
