package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.UserNameDTO;
import com.savvato.tribeapp.entities.Cosign;
import com.savvato.tribeapp.entities.CosignId;
import com.savvato.tribeapp.repositories.CosignRepository;
import com.savvato.tribeapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CosignServiceImpl implements CosignService {

    @Autowired
    CosignRepository cosignRepository;

    @Autowired
    UserRepository userRepository;

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

    @Override
    public boolean deleteCosign(Long userIdIssuing, Long userIdReceiving, Long phraseId) {
        return false;
    }

    @Override
    public List<UserNameDTO> getCosignersForUserAttribute(Long userIdReceiving, Long phraseId) {

        List<UserNameDTO> list = new ArrayList<>();
        List<Long> cosignerIds = cosignRepository.findCosignersByUserIdReceivingAndPhraseId(userIdReceiving, phraseId);

        for(Long id : cosignerIds) {
            UserNameDTO user = UserNameDTO.builder()
                .userId(id)
                .userName(userRepository.findById(id).get().getName())
                .build();
            list.add(user);
        }

        return list;
    }
}
