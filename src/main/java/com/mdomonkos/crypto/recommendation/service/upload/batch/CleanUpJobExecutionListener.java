package com.mdomonkos.crypto.recommendation.service.upload.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CleanUpJobExecutionListener implements JobExecutionListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(CleanUpJobExecutionListener.class);

  @Override
  public void beforeJob(JobExecution jobExecution) {
    String fileLocation = jobExecution.getJobParameters().getString("fileLocation");
    LOGGER.debug("Job {} started for {}", jobExecution.getJobId(), fileLocation);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    String fileLocation = jobExecution.getJobParameters().getString("fileLocation");
    LOGGER.debug("Job {} finished for {}", jobExecution.getJobId(), fileLocation);
    deleteFile(fileLocation);
  }

  private void deleteFile(String fileLocation) {
    try {
      Files.delete(Paths.get(fileLocation));
    } catch (Exception e) {
      LOGGER.error(String.format("Failed to clear file %s", fileLocation), e);
    }
  }
}
