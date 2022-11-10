package com.mdomonkos.crypto.recommendation.service.upload.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoRaw {

  private Long timestamp;
  private String symbol;
  private Double price;
}
