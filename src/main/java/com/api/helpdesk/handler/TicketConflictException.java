package com.api.helpdesk.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TicketConflictException extends RuntimeException {
    public TicketConflictException(String message) {
        super(message);
    }
}
