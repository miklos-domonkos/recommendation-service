package com.mdomonkos.crypto.recommendation.service.repository;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
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

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles(profiles = "int")
@Import(MockSpringBatchConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"spring.main.allow-bean-definition-overriding=true", "batch.enabled=false"})
//@ContextConfiguration(initializers = {PostgresStatisticsRepository5IT.Initializer.class})
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

//  @MockBean
//  public JobLauncher jobLauncher;
//
//  @MockBean
//  public Job job;

  private static final String SYMBOL1 = "A";
  private static final String SYMBOL2 = "B";
  private static final Date DATE1 = new Date(1641009600000L);
  private static final Date DATE2 = new Date(1641020400000L);
  private static final Double PRICE1 = 10.0;
  private static final Double PRICE2 = 20.0;

  @Autowired
  private StatisticsRepository statisticsRepository;

  @Autowired
  private EntityManager entityManager;

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
      new Crypto(new Crypto.CryptoId(SYMBOL1, DATE1), PRICE1),
      new Crypto(new Crypto.CryptoId(SYMBOL2, DATE1), PRICE1),
      new Crypto(new Crypto.CryptoId(SYMBOL2, DATE2), PRICE2)
    ));

    // when
    List<String> result = statisticsRepository.getSymbolsOrderedByRange();

    // then
    assertThat(result).isEqualTo(List.of(SYMBOL2, SYMBOL1));
  }
}