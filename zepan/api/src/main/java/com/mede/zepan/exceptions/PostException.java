package com.mede.zepan.exceptions;

/**
 *
 */
public class PostException extends ZepanException {
    public PostException() {
    }

    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostException(Throwable cause) {
        super(cause);
    }

    public PostException(String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getName() {
        return "POST_ERROR";
    }
}
