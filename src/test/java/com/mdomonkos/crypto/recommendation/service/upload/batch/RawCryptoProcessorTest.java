package com.mdomonkos.crypto.recommendation.service.upload.batch;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
import com.mdomonkos.crypto.recommendation.service.upload.model.CryptoRaw;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RawCryptoProcessorTest {

  private final RawCryptoProcessor rawCryptoProcessor = new RawCryptoProcessor();

  @Test
  public void converts_raw_data_to_entity() throws Exception {
    // given
    CryptoRaw raw = new CryptoRaw(2L, "A", 1.1);
    Crypto expected = new Crypto(new Crypto.CryptoId("A", new Date(2L)), 1.1);

    // when
    Crypto result = rawCryptoProcessor.process(raw);

    //then
    assertEquals(expected, result);

  }
}