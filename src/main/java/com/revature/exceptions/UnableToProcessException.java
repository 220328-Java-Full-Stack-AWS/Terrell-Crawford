package com.revature.exceptions;

public class UnableToProcessException extends RuntimeException{
    public UnableToProcessException() {
        super();
    }

    public UnableToProcessException(String message) {
        super(message);
    }

    public UnableToProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToProcessException(Throwable cause) {
        super(cause);
    }

    public UnableToProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
