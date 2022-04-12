package com.revature.exceptions;

public class EmailNotUnique extends RuntimeException{
    public EmailNotUnique() {
        super();
    }

    public EmailNotUnique(String message) {
        super(message);
    }

    public EmailNotUnique(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotUnique(Throwable cause) {
        super(cause);
    }

    public EmailNotUnique(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
