package com.dreamteam.eduuca.controlleradvices;

import com.dreamteam.eduuca.payload.response.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Log4j2
@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurity(SecurityException e, HttpServletRequest request) {
        log.error("security exception");
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        log.error("illegal argument exception");
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e, HttpServletRequest request) {
        log.error("illegal state exception");
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException e, HttpServletRequest request) {
        log.error("entity not found");
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }
}
