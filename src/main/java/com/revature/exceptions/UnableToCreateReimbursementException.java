package com.revature.exceptions;

public class UnableToCreateReimbursementException extends RuntimeException{
    public UnableToCreateReimbursementException() {
        super();
    }

    public UnableToCreateReimbursementException(String message) {
        super(message);
    }

    public UnableToCreateReimbursementException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToCreateReimbursementException(Throwable cause) {
        super(cause);
    }

    public UnableToCreateReimbursementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
