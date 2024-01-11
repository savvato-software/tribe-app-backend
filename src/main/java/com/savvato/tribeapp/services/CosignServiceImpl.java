package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.CosignRequest;
import com.savvato.tribeapp.entities.Cosign;
import com.savvato.tribeapp.repositories.CosignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CosignServiceImpl implements CosignService {

    @Autowired
    CosignRepository cosignRepository;

    @Override
    public void saveCosign(CosignRequest cosignRequest) {

        Cosign cosign = new Cosign();
        cosign.setUserIdIssuing(cosignRequest.userIdIssuing);
        cosign.setUserIdReceiving(cosignRequest.userIdReceiving);
        cosign.setPhraseId(cosignRequest.phraseId);
        cosignRepository.save(cosign);
        log.info("Cosign from user: " + cosignRequest.userIdIssuing + " to user: " + cosignRequest.userIdReceiving + " added." );
    }
}
