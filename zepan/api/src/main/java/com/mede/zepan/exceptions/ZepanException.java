package com.mede.zepan.exceptions;

/**
 * Created by JWang on 5/25/2017.
 */
public class ZepanException extends Exception {
    public ZepanException() {
    }

    public ZepanException(String message) {
        super(message);
    }

    public ZepanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZepanException(Throwable cause) {
        super(cause);
    }

    public ZepanException(String message,
                            Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public String getName() {
        return "ZEPAN_ERROR";
    }
}
