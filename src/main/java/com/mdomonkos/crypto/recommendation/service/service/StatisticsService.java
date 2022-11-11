package com.mdomonkos.crypto.recommendation.service.service;

import com.mdomonkos.crypto.recommendation.service.exception.ResourceNotFoundException;
import com.mdomonkos.crypto.recommendation.service.model.Statistics;
import com.mdomonkos.crypto.recommendation.service.repository.CryptoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Provides statistics about cryptos.
 */
@Service
@Slf4j
public class StatisticsService {

  private CryptoRepository cryptoRepository;

  public StatisticsService(CryptoRepository cryptoRepository) {
    this.cryptoRepository = cryptoRepository;
  }

  /**
   * Descending sorted list of all the cryptos,
   * comparing the normalized range (i.e. (max-min)/min).
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
   * @throws ResourceNotFoundException If there is no information for the given crypto
   */
  public Statistics getStatistics(String symbol) throws ResourceNotFoundException {
    Statistics statistics = cryptoRepository.findStatisticsBySymbol(symbol);
    if (Objects.isNull(statistics.getSymbol())) {
      throw new ResourceNotFoundException("No information available for " + symbol);
    }
    return statistics;
  }

  /**
   * Returns crypto with the highest normalized range for a specific day from the starting date/time.
   *
   * @param startDateTime the starting date/time
   * @return The symbol of the recommended crypto
   * @throws ResourceNotFoundException If there is no information for the given day
   */
  public String getCryptoWithHighestNormalizedRangeForDay(Date startDateTime) {
    Date endDateTime = Date.from(startDateTime.toInstant().plus(Duration.ofDays(1)));
    return cryptoRepository.getCryptoWithHighestNormalizedRangeForRange(startDateTime, endDateTime)
                           .orElseThrow(() -> new ResourceNotFoundException(
                             "No information available for date" + startDateTime));
  }
}
