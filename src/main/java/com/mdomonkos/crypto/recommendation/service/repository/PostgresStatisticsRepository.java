package com.mdomonkos.crypto.recommendation.service.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class PostgresStatisticsRepository implements StatisticsRepository {

  private final EntityManager entityManager;

  public PostgresStatisticsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public List<String> getSymbolsOrderedByRange() {
    return entityManager.createNativeQuery("SELECT symbol FROM " +
                                             "(SELECT symbol, min(price), max(price) FROM crypto.crypto GROUP BY symbol) stat " +
                                             "ORDER BY (max-min)/min DESC").getResultList();

  }
}
