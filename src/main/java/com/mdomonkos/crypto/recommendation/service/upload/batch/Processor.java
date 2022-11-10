package com.mdomonkos.crypto.recommendation.service.upload.batch;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
import com.mdomonkos.crypto.recommendation.service.upload.model.CryptoRaw;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Processor implements ItemProcessor<CryptoRaw, Crypto> {

  @Override
  public Crypto process(CryptoRaw crypto) throws Exception {
    return new Crypto(new Crypto.CryptoId(crypto.getSymbol(), new Date(crypto.getTimestamp())), crypto.getPrice());
  }
}
