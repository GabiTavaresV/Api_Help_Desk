package com.api.helpdesk.exception;

public class AttendantAlreadyExistsException extends RuntimeException {
    public AttendantAlreadyExistsException(String message) {
        super(message);
    }
}
