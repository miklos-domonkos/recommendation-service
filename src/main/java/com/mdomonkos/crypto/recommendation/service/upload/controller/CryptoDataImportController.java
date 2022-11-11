package com.mdomonkos.crypto.recommendation.service.upload.controller;

import com.mdomonkos.crypto.recommendation.service.exception.ResourceNotFoundException;
import com.mdomonkos.crypto.recommendation.service.exception.UploadFailedException;
import com.mdomonkos.crypto.recommendation.service.model.JobExecutionDetailsResponse;
import com.mdomonkos.crypto.recommendation.service.upload.service.TempFileImportService;
import com.mdomonkos.crypto.recommendation.service.upload.service.TempFileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for cvs import.
 */
@RestController
@RequestMapping("import")
@Slf4j
public class CryptoDataImportController {

  @Autowired
  private JobExplorer jobExplorer;

  @Autowired
  private TempFileUploadService uploadService;

  @Autowired
  private TempFileImportService importService;

  private static final Logger LOGGER = LoggerFactory.getLogger(CryptoDataImportController.class);

  /**
   * Upload a single cvs file with crypto data into the database.
   *
   * @param file MultipartFile file
   * @return Import Job execution details
   * @throws UploadFailedException File upload failed
   */
  @Operation(summary = "Upload a single cvs file with crypto data into the database")
  @PostMapping("file")
  public JobExecutionDetailsResponse upload(@RequestPart("file") MultipartFile file) throws UploadFailedException {

    String localPath = uploadService.upload(file);
    return importService.process(localPath);
  }

  /**
   * Upload a multiple cvs file with crypto data into the database.
   *
   * @param files List of MultipartFile file
   * @return List of Import Job execution details
   * @throws UploadFailedException File upload failed
   */
  @Operation(summary = "Upload a multiple cvs file with crypto data into the database")
  @PostMapping("files")
  public List<JobExecutionDetailsResponse> upload(@RequestPart("files") List<MultipartFile> files) throws UploadFailedException {
    List<String> tempFiles = uploadService.upload(files);

    return tempFiles.stream()
                    .map(importService::process)
                    .collect(Collectors.toList());
  }

  /**
   * Get Import Job Details by job id.
   *
   * @param jobId Job's id
   * @return Import Job execution details
   * @throws ResourceNotFoundException Job does not exist
   */
  @Operation(summary = "Get Import Job Details")
  @GetMapping("jobs/{jobId}")
  public JobExecutionDetailsResponse jobDetails(@PathVariable("jobId") long jobId) throws ResourceNotFoundException {
    return Optional.ofNullable(jobExplorer.getJobExecution(jobId))
                   .map(jobExecution -> new JobExecutionDetailsResponse(jobExecution.getJobId(), jobExecution.getStatus().name()))
                   .orElseThrow(() -> new ResourceNotFoundException("Job with id: " + jobId + " was not found"));
  }
}
