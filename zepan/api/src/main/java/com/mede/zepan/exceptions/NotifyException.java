package com.mede.zepan.exceptions;

/**
 *
 */
public class NotifyException extends ZepanException {

    public NotifyException() {
    }

    public NotifyException(String message) {
        super(message);
    }

    public NotifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotifyException(Throwable cause) {
        super(cause);
    }

    public NotifyException(String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getName() {
        return "NOTIFY_ERROR";
    }
}
