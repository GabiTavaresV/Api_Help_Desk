package com.api.helpdesk.exception;

public class DeviceAlreadyExistsException  extends RuntimeException{
    public DeviceAlreadyExistsException(String message) {
        super(message);
    }
}
