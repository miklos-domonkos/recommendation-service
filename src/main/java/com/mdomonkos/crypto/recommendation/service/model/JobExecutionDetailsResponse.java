package com.mdomonkos.crypto.recommendation.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * POJO holds Job Execution Details
 */
@Data
@AllArgsConstructor
public class JobExecutionDetailsResponse {
  private Long jobId;
  private String status;
}
