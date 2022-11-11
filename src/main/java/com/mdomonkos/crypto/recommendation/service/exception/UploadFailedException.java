package com.mdomonkos.crypto.recommendation.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to handle file upload cases.
 * Results {@link HttpStatus#INTERNAL_SERVER_ERROR}
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UploadFailedException extends Exception {

  public UploadFailedException(String message) {
    super(message);
  }
}
