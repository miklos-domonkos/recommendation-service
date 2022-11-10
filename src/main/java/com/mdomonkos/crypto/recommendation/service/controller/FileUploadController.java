package com.mdomonkos.crypto.recommendation.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("upload")
public class FileUploadController {

  @Autowired
  JobLauncher jobLauncher;

  @Autowired
  Job job;
  private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

  @Operation(summary = "Upload a single cvs file with crypto data")
  @PostMapping("file/single")
  public String upload(@RequestPart("fileToUpload") MultipartFile file) {

    File tmpFile;
    try {
      tmpFile = File.createTempFile("fileToUpload-", file.getName());
      tmpFile.deleteOnExit();
      LOGGER.debug(tmpFile.getAbsolutePath());
      file.transferTo(tmpFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Map<String, JobParameter> maps = new HashMap<>();
    maps.put("fileLocation", new JobParameter(tmpFile.getAbsolutePath()));
    JobParameters parameters = new JobParameters(maps);
    JobExecution jobExecution = null;

    try {
      jobExecution = jobLauncher.run(job, parameters);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    while (jobExecution.isRunning()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return jobExecution.getStatus().name();
  }
}
