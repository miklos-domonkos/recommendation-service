package com.mdomonkos.crypto.recommendation.service.upload.service;

import com.mdomonkos.crypto.recommendation.service.model.JobExecutionDetailsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Import crypto data from local path.
 */
@Service
@Slf4j
public class TempFileImportService {

  private JobLauncher asyncJobLauncher;

  private Job job;

  public TempFileImportService(JobLauncher asyncJobLauncher, Job job) {
    this.asyncJobLauncher = asyncJobLauncher;
    this.job = job;
  }

  public JobExecutionDetailsResponse process(String localPath) {

    JobParameters parameters = getJobParameters(localPath);
    JobExecution jobExecution = null;

    try {
      jobExecution = asyncJobLauncher.run(job, parameters);
    } catch (JobInstanceAlreadyCompleteException
             | JobExecutionAlreadyRunningException
             | JobParametersInvalidException
             | JobRestartException e) {
      log.error(e.getMessage(), e);
    }
    return new JobExecutionDetailsResponse(jobExecution.getJobId(), jobExecution.getStatus().name());
  }

  private JobParameters getJobParameters(String localPath) {
    Map<String, JobParameter> maps = new HashMap<>();
    maps.put("fileLocation", new JobParameter(localPath));
    JobParameters parameters = new JobParameters(maps);
    return parameters;
  }
}
