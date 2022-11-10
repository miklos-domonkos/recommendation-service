package com.mdomonkos.crypto.recommendation.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;

/**
 *  Crypto details
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crypto {

  @EmbeddedId
  private CryptoId id;
  private Double price;

  /**
   * Crypto composite id
   */
  @Embeddable
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CryptoId implements Serializable {

    private String symbol;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date timestamp;

  }
}
