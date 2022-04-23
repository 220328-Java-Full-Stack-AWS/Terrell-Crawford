package com.revature.exceptions;

public class NoSuchReimbursementException extends RuntimeException{
    public NoSuchReimbursementException() {
        super();
    }

    public NoSuchReimbursementException(String message) {
        super(message);
    }

    public NoSuchReimbursementException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchReimbursementException(Throwable cause) {
        super(cause);
    }

    public NoSuchReimbursementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
