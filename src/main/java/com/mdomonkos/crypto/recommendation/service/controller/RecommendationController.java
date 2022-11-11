package com.mdomonkos.crypto.recommendation.service.controller;

import com.mdomonkos.crypto.recommendation.service.model.Statistics;
import com.mdomonkos.crypto.recommendation.service.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * Controller for getting different kind of recommendations.
 */
@RestController
@RequestMapping("recommendations")
@Slf4j
@Validated
public class RecommendationController {

  private StatisticsService statisticsService;

  public RecommendationController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  /**
   * Descending sorted list of all the cryptos comparing the normalized range (i.e. (max-min)/min)
   *
   * @return Ordered list of cryptos' symbol
   */
  @Operation(summary = "Descending sorted list of all the cryptos comparing the normalized range (i.e. (max-min)/min)")
  @GetMapping("")
  public List<String> recommendation() {
    return statisticsService.getRecommendation();
  }

  /**
   * Returns statistics for the requested crypto.
   *
   * @param symbol symbol of the crypto
   * @return The statistics
   */
  @Operation(summary = "Returns statistics for the requested crypto")
  @GetMapping("cryptos/{symbol}/statistics")
  public Statistics statistics(@PathVariable("symbol") @NotBlank String symbol) {
    log.info(symbol);
    return statisticsService.getStatistics(symbol);
  }

  /**
   * Returns crypto with the highest normalized range for a specific day from the starting date/time.
   *
   * @param day ISO Date/time for the date start, must be in format yyyy-MM-dd'T'00:00:00X
   * @return symbol of the crypto
   */
  @Operation(summary = "Returns crypto with the highest normalized range for a specific day from the starting date/time")
  @GetMapping("/volatile")
  public String cryptoWithHighestNormalizedRangeForDay(
    @Parameter(
      required = true,
      description = "ISO Date/time for the date start, must be in format yyyy-MM-dd'T'00:00:00X",
      example = "2022-01-01T00:00:00+05:00"

    )
    @RequestParam("day")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'00:00:00X")
    Date day
  ) {
    return statisticsService.getCryptoWithHighestNormalizedRangeForDay(day);
  }
}
