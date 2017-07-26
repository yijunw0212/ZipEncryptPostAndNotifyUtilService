package com.mede.zepan;

import java.io.OutputStream;

import com.mede.zepan.exceptions.ZepanException;

/**
 *
 */
public interface OutputStreamDecorator {

    OutputStream decorate(OutputStream outputStream) throws ZepanException;
}
