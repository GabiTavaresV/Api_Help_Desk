package com.api.helpdesk.exception;

public class NotFoundDBException extends  RuntimeException {
     public NotFoundDBException(String message) {
        super(message);
    }
    public NotFoundDBException(String message, Throwable cause) {
        super(message, cause);
    }
}
