package com.mdomonkos.crypto.recommendation.service.testconfig;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class MockSpringBatchConfiguration {

  @MockBean
  public JobLauncher jobLauncher;

  @MockBean
  public Job job;
}
