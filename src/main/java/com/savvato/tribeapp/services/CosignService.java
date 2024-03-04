package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.UserNameDTO;

import java.util.List;
import java.util.Optional;

public interface CosignService {

    CosignDTO saveCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);
    boolean deleteCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId);

    List<UserNameDTO> getCosignersForUserAttribute(Long userReceivingId, Long phraseId);
}
