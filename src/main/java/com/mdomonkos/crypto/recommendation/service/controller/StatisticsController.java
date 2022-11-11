package com.mdomonkos.crypto.recommendation.service.controller;

import com.mdomonkos.crypto.recommendation.service.model.Statistics;
import com.mdomonkos.crypto.recommendation.service.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Controller for getting different kind of recommendations.
 */
@RestController
@RequestMapping("statistics")
@Slf4j
@Validated
public class StatisticsController {

  private StatisticsService statisticsService;

  public StatisticsController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @Operation(summary = "Descending sorted list of all the cryptos,\n"
    + "comparing the normalized range (i.e. (max-min)/min)")
  @GetMapping("recommendation")
  public List<String> recommendation() {
    return statisticsService.getRecommendation();
  }

  @Operation(summary = "Returns statistics for the requested crypto")
  @GetMapping("/{symbol}")
  public Statistics statistics(@PathVariable("symbol") @NotBlank String symbol) {
    log.info(symbol);
    return statisticsService.getStatistics(symbol);
  }

}
