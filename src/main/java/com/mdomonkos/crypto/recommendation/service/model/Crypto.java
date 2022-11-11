package com.mdomonkos.crypto.recommendation.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Crypto details.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(
  name = "statisticsResult",
  classes = {
    @ConstructorResult(
      targetClass = Statistics.class,
      columns = {
        @ColumnResult(name = "min", type = Double.class),
        @ColumnResult(name = "max", type = Double.class),
        @ColumnResult(name = "oldest", type = Date.class),
        @ColumnResult(name = "newest", type = Date.class),
        @ColumnResult(name = "symbol", type = String.class),
      }
    )
  }
)

@NamedNativeQuery(name = "Crypto.findStatisticsBySymbol",
  resultClass = Statistics.class,
  resultSetMapping = "statisticsResult",
  query = "SELECT min(price) as min,"
    + " min(timestamp) as oldest, "
    + "max(price) as max, "
    + "max(timestamp) as newest, "
    + "min(symbol) as symbol "
    + "FROM crypto.crypto "
    + "WHERE symbol = :symbol"
)
public class Crypto {

  @EmbeddedId
  private CryptoId id;
  @NotNull
  private Double price;

  /**
   * Crypto composite id.
   */
  @Embeddable
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CryptoId implements Serializable {

    private String symbol;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private java.util.Date timestamp;

  }
}
