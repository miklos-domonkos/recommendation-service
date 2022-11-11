package com.mdomonkos.crypto.recommendation.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetailsResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
    ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetailsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
