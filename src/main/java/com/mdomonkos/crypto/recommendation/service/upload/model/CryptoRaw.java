package com.mdomonkos.crypto.recommendation.service.upload.model;

import lombok.Data;

@Data
public class CryptoRaw {

  private Long timestamp;
  private String symbol;
  private Double price;
}
