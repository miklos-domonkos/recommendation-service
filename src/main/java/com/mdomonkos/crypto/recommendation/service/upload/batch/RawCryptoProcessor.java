package com.mdomonkos.crypto.recommendation.service.upload.batch;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
import com.mdomonkos.crypto.recommendation.service.upload.model.CryptoRaw;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class RawCryptoProcessor implements ItemProcessor<CryptoRaw, Crypto> {

  @Override
  public Crypto process(CryptoRaw crypto) throws Exception {
    Objects.requireNonNull(crypto.getPrice());
    Objects.requireNonNull(crypto.getSymbol());
    Objects.requireNonNull(crypto.getTimestamp());
    return new Crypto(new Crypto.CryptoId(crypto.getSymbol(), new Date(crypto.getTimestamp())), crypto.getPrice());
  }
}
