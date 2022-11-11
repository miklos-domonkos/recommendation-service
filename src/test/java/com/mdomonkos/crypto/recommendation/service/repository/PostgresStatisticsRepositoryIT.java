package com.mdomonkos.crypto.recommendation.service.repository;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
import com.mdomonkos.crypto.recommendation.service.model.Statistics;
import com.mdomonkos.crypto.recommendation.service.testconfig.MockSpringBatchConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles(profiles = "int")
@Import(MockSpringBatchConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"spring.main.allow-bean-definition-overriding=true", "batch.enabled=false"})
public class PostgresStatisticsRepositoryIT {

  @Container
  public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:13.3-alpine")
    .withDatabaseName("recommendation_storage")
    .withUsername("root")
    .withPassword("root")
    .withInitScript("initpostgre.sql");

  @DynamicPropertySource
  public static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl() + "&currentSchema=crypto");
    registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
  }

  private static final String SYMBOL1 = "A";
  private static final String SYMBOL2 = "B";
  private static final long OLD_EPOCH = 1641009600000L;
  private static final long NEW_EPOCH = 1641020400000L;
  private static final Date OLD = new Date(OLD_EPOCH);
  private static final Date NEW = new Date(NEW_EPOCH);
  private static final Double LOW = 10.0;
  private static final Double HIGH = 20.0;

  @Autowired
  private CryptoRepository cryptoRepository;

  @AfterEach
  public void cleanDatabase() {
    cryptoRepository.deleteAll();
  }

  @Test
  public void can_get_recommendation_in_descending_range_order() {
    // given
    cryptoRepository.saveAll(List.of(
      new Crypto(new Crypto.CryptoId(SYMBOL1, OLD), LOW),
      new Crypto(new Crypto.CryptoId(SYMBOL2, OLD), LOW),
      new Crypto(new Crypto.CryptoId(SYMBOL2, NEW), HIGH)
    ));

    // when
    List<String> result = cryptoRepository.getSymbolsOrderedByRange();

    // then
    assertThat(result).isEqualTo(List.of(SYMBOL2, SYMBOL1));
  }

  @Test
  public void can_get_statistics_correctly() {
    // given
    cryptoRepository.saveAll(List.of(
      new Crypto(new Crypto.CryptoId(SYMBOL1, OLD), LOW),
      new Crypto(new Crypto.CryptoId(SYMBOL2, OLD), LOW),
      new Crypto(new Crypto.CryptoId(SYMBOL2, NEW), HIGH)
    ));
    // when
    Statistics result = cryptoRepository.findStatisticsBySymbol(SYMBOL2);

    // then
    assertThat(result.getOldest().toInstant().toEpochMilli()).isEqualTo(OLD_EPOCH);
    assertThat(result.getNewest().toInstant().toEpochMilli()).isEqualTo(NEW_EPOCH);
    assertThat(result.getMin()).isEqualTo(LOW);
    assertThat(result.getMax()).isEqualTo(HIGH);
    assertThat(result.getSymbol()).isEqualTo(SYMBOL2);
  }

  @Test
  public void returns_statistics_with_symbol_null_if_not_found() {
    // given
    cryptoRepository.saveAll(List.of(
      new Crypto(new Crypto.CryptoId(SYMBOL2, OLD), LOW),
      new Crypto(new Crypto.CryptoId(SYMBOL2, NEW), HIGH)
    ));
    // when
    Statistics result = cryptoRepository.findStatisticsBySymbol(SYMBOL1);

    // then
    assertThat(result.getSymbol()).isEqualTo(null);
  }
}