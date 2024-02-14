package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.CosignRequest;
import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.entities.Cosign;
import com.savvato.tribeapp.repositories.CosignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CosignServiceImpl implements CosignService {

    @Autowired
    CosignRepository cosignRepository;

    @Override
    public CosignDTO saveCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId) {

        Cosign cosign = new Cosign();
        cosign.setUserIdIssuing(userIdIssuing);
        cosign.setUserIdReceiving(userIdReceiving);
        cosign.setPhraseId(phraseId);

        Cosign savedCosign = cosignRepository.save(cosign);
        log.info("Cosign from user " + userIdIssuing + " to user " + userIdReceiving + " added." );

        CosignDTO cosignDTO = CosignDTO
                .builder()
                .userIdIssuing(savedCosign.getUserIdIssuing())
                .userIdReceiving(savedCosign.getUserIdReceiving())
                .phraseId(savedCosign.getPhraseId())
                .build();

        return cosignDTO;
    }
}
