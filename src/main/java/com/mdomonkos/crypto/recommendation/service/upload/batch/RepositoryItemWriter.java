package com.mdomonkos.crypto.recommendation.service.upload.batch;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
import com.mdomonkos.crypto.recommendation.service.repository.CryptoRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepositoryItemWriter implements ItemWriter<Crypto> {

  @Autowired
  private CryptoRepository cryptoRepository;

  @Override
  public void write(List<? extends Crypto> cryptos) throws Exception {
    cryptoRepository.saveAll(cryptos);
  }
}
