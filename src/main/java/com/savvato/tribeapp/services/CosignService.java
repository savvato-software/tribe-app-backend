package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.CosignsForUserDTO;
import com.savvato.tribeapp.dto.UserNameDTO;

import java.util.List;

public interface CosignService {

    CosignDTO saveCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);
    boolean deleteCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);

    List<UserNameDTO> getCosignersForUserAttribute(Long userReceivingId, Long phraseId);

    List<CosignsForUserDTO> getAllCosignsForUser(Long userIdReceiving);
}
