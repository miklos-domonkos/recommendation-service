package com.mdomonkos.crypto.recommendation.service.upload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Raw data of crypto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoRaw {

  private Long timestamp;
  private String symbol;
  private Double price;
}
