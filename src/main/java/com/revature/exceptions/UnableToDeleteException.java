package com.revature.exceptions;

public class UnableToDeleteException extends RuntimeException{
    public UnableToDeleteException() {
        super();
    }

    public UnableToDeleteException(String message) {
        super(message);
    }

    public UnableToDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToDeleteException(Throwable cause) {
        super(cause);
    }

    public UnableToDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
