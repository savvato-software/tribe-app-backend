package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;

import java.util.Optional;

public interface CosignService {

    CosignDTO saveCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);

    void deleteCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);
}
