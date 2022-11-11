package com.mdomonkos.crypto.recommendation.service.service;

import com.mdomonkos.crypto.recommendation.service.exception.ResourceNotFoundException;
import com.mdomonkos.crypto.recommendation.service.model.Statistics;
import com.mdomonkos.crypto.recommendation.service.repository.CryptoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Provides statistics about cryptos.
 */
@Service
public class StatisticsService {

  private CryptoRepository cryptoRepository;

  public StatisticsService(CryptoRepository cryptoRepository) {
    this.cryptoRepository = cryptoRepository;
  }

  /**
   * Descending sorted list of all the cryptos,
   *  comparing the normalized range (i.e. (max-min)/min).
   *
   * @return The ordered list of cryptos
   */
  public List<String> getRecommendation() {
    return cryptoRepository.getSymbolsOrderedByRange();
  }

  /**
   * Returns statistics for the requested crypto.
   *
   * @param symbol Symbol of the crypto
   * @return {@link Statistics}
   * @throws ResourceNotFoundException If no information for the crypto
   */
  public Statistics getStatistics(String symbol) throws ResourceNotFoundException {
    Statistics nullableStatistics = cryptoRepository.findStatisticsBySymbol(symbol);
    if (Objects.isNull(nullableStatistics.getSymbol())) {
      throw new ResourceNotFoundException("No information available for " + symbol);
    }
    return nullableStatistics;
  }
}
