package com.api.helpdesk.exception;

public class SoftDeleteException extends RuntimeException {
    public SoftDeleteException(String message) {
        super(message);
    }
}
