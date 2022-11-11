package com.mdomonkos.crypto.recommendation.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * POJO holds statistics for a crypto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Statistics {
  private Double min;
  private Double max;
  private Date oldest;
  private Date newest;
  private String symbol;
}
