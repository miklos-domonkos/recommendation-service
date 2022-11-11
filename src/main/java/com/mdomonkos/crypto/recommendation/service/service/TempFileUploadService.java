package com.mdomonkos.crypto.recommendation.service.service;

import com.mdomonkos.crypto.recommendation.service.exception.UploadFailedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * Uploads source to temporary file.
 */
@Slf4j
@Service
public class TempFileUploadService {
  /**
   * Uploads source to temporary file.
   *
   * Cleans up temporary file on error.
   *
   * @param source Source to upload
   * @return the temporary file's absolute path
   * @throws UploadFailedException If upload is not successful.
   */
  public String upload(InputStreamSource source) throws UploadFailedException {
    File tmpFile = null;
    try {
      tmpFile = File.createTempFile("temp-", "");
      tmpFile.deleteOnExit();
      log.debug(tmpFile.getAbsolutePath());
      InputStream inputStream = source.getInputStream();
      Files.copy(
        source.getInputStream(),
        tmpFile.toPath(),
        StandardCopyOption.REPLACE_EXISTING);

      IOUtils.closeQuietly(inputStream);
      return tmpFile.getAbsolutePath();
    } catch (IOException e) {
      if (!Objects.isNull(tmpFile) && tmpFile.exists()) {
        deleteFile(tmpFile.getAbsolutePath());
      }
      throw new UploadFailedException("File upload failed");
    }
  }

  private void deleteFile(String fileLocation) {
    try {
      Files.deleteIfExists(Paths.get(fileLocation));
    } catch (Exception e) {
      log.error(String.format("Failed to clear file %s", fileLocation), e);
    }
  }
}
