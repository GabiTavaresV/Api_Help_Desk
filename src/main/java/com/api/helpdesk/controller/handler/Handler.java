package com.api.helpdesk.controller.handler;

import com.api.helpdesk.exception.NotFoundDBException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class Handler {
 @ExceptionHandler(NotFoundDBException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundDBException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
