package com.mdomonkos.crypto.recommendation.service.repository;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
import com.mdomonkos.crypto.recommendation.service.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Run crypto related queries.
 */
public interface CryptoRepository extends JpaRepository<Crypto, String> {

  /**
   * Returns statistics for the requested crypto.
   * If no information available returns details with null.
   *
   * @param symbol Symbol of the requested crypto
   * @return {@link Statistics}
   */
  @Query(nativeQuery = true)
  Statistics findStatisticsBySymbol(@Param("symbol") String symbol);

  /**
   * Descending sorted list of all the cryptos,
   * comparing the normalized range (i.e. (max-min)/min)
   *
   * @return The ordered list of cryptos
   */
  @Query(nativeQuery = true, value = "SELECT symbol FROM "
    + "(SELECT symbol, min(price), max(price) FROM crypto.crypto GROUP BY symbol) stat "
    + "ORDER BY (max-min)/min DESC")
  List<String> getSymbolsOrderedByRange();

}
