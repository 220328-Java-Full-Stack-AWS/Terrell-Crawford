package com.revature.exceptions;

public class NoSuchPassword extends RuntimeException{
    public NoSuchPassword() {
        super();
    }

    public NoSuchPassword(String message) {
        super(message);
    }

    public NoSuchPassword(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchPassword(Throwable cause) {
        super(cause);
    }

    public NoSuchPassword(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
