package com.mede.zepan.exceptions;

/**
 *
 */
public class ZipException extends ZepanException {

    public ZipException() {
    }

    public ZipException(String message) {
        super(message);
    }

    public ZipException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZipException(Throwable cause) {
        super(cause);
    }

    public ZipException(String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getName() {
        return "ZIP_ERROR";
    }
}
