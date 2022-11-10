package com.mdomonkos.crypto.recommendation.service.repository;

import com.mdomonkos.crypto.recommendation.service.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRepository extends JpaRepository<Crypto, String> {

}
