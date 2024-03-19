package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.CosignsForUserDTO;
import com.savvato.tribeapp.dto.UsernameDTO;

import java.util.List;

public interface CosignService {

    Optional<CosignDTO> saveCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);
    boolean deleteCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);

    List<UsernameDTO> getCosignersForUserAttribute(Long userReceivingId, Long phraseId);

    List<CosignsForUserDTO> getAllCosignsForUser(Long userIdReceiving);
}
