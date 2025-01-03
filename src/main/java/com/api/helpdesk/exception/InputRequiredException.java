package com.api.helpdesk.exception;

public class InputRequiredException  extends RuntimeException{
    public InputRequiredException(String message) {
        super(message);
    }
}
