package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;

import java.util.Optional;

public interface CosignService {

    Optional<CosignDTO> saveCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);
    boolean deleteCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);
}
