package com.mdomonkos.crypto.recommendation.service.controller;

import com.mdomonkos.crypto.recommendation.service.exception.ResourceNotFoundException;
import com.mdomonkos.crypto.recommendation.service.exception.UploadFailedException;
import com.mdomonkos.crypto.recommendation.service.model.JobExecutionDetailsResponse;
import com.mdomonkos.crypto.recommendation.service.service.TempFileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for cvs import.
 */
@RestController
@RequestMapping("upload")
public class FileUploadController {

  @Autowired
  private JobLauncher asyncJobLauncher;

  @Autowired
  private JobExplorer jobExplorer;

  @Autowired
  private TempFileUploadService uploadService;

  @Autowired
  private Job job;
  private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

  @Operation(summary = "Upload a single cvs file with crypto data")
  @PostMapping("file/single")
  public JobExecutionDetailsResponse upload(@RequestPart("fileToUpload") MultipartFile file) throws UploadFailedException {

    String localPath = uploadService.upload(file);

    Map<String, JobParameter> maps = new HashMap<>();
    maps.put("fileLocation", new JobParameter(localPath));
    JobParameters parameters = new JobParameters(maps);
    JobExecution jobExecution = null;

    try {
      jobExecution = asyncJobLauncher.run(job, parameters);
    } catch (JobInstanceAlreadyCompleteException
             | JobExecutionAlreadyRunningException
             | JobParametersInvalidException
             | JobRestartException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return new JobExecutionDetailsResponse(jobExecution.getJobId(), jobExecution.getStatus().name());
  }

  @Operation(summary = "Get Import Job Details")
  @GetMapping("jobs/{jobId}")
  public JobExecutionDetailsResponse jobDetails(@PathVariable("jobId") long jobId) throws ResourceNotFoundException {
    return Optional.ofNullable(jobExplorer.getJobExecution(jobId))
                   .map(jobExecution -> new JobExecutionDetailsResponse(jobExecution.getJobId(), jobExecution.getStatus().name()))
                   .orElseThrow(() -> new ResourceNotFoundException("Job with id: " + jobId + " was not found"));
  }
}
