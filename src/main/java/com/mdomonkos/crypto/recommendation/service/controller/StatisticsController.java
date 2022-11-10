package com.mdomonkos.crypto.recommendation.service.controller;

import com.mdomonkos.crypto.recommendation.service.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("statistics")
public class StatisticsController {

  private StatisticsService statisticsService;

  public StatisticsController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @Operation(summary = "Descending sorted list of all the cryptos,\n" +
    "comparing the normalized range (i.e. (max-min)/min)")
  @GetMapping("recommendation")
  public List<String> recommendation() {
    return statisticsService.getRecommendation();
  }
}
