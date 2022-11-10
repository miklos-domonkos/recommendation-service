package com.mdomonkos.crypto.recommendation.service.service;

import com.mdomonkos.crypto.recommendation.service.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

  private StatisticsRepository statisticsRepository;

  public StatisticsService(StatisticsRepository statisticsRepository) {
    this.statisticsRepository = statisticsRepository;
  }

  public List<String> getRecommendation() {
    return statisticsRepository.getSymbolsOrderedByRange();
  }
}
