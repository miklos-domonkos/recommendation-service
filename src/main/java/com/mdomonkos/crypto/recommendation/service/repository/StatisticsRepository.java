package com.mdomonkos.crypto.recommendation.service.repository;

import java.util.List;

public interface StatisticsRepository {
  List<String> getSymbolsOrderedByRange();
}
