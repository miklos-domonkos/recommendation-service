package com.mdomonkos.crypto.recommendation.service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * POJO for errors.
 */
@Data
@AllArgsConstructor
public class ErrorDetailsResponse {
  private Date timestamp;
  private String message;
  private String details;
}
