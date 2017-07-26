package com.mede.zepan;

import java.io.InputStream;

import com.mede.zepan.exceptions.ZepanException;

/**
 *
 */
public interface InputStreamDecorator {

    InputStream decorate(InputStream inputStream) throws ZepanException;
}
