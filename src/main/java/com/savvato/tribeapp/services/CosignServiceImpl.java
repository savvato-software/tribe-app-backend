package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.CosignsForUserDTO;
import com.savvato.tribeapp.dto.UserNameDTO;
import com.savvato.tribeapp.entities.Cosign;
import com.savvato.tribeapp.repositories.CosignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CosignServiceImpl implements CosignService {

    @Autowired
    CosignRepository cosignRepository;

    @Autowired
    UserService userService;

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
            list.add(userService.getUserNameDTO(id));
        }

        return list;
    }

    @Override
    public List<CosignsForUserDTO> getAllCosignsForUser(Long userIdReceiving) {

        List<CosignsForUserDTO> cosignsForUserDTOs = new ArrayList<>();

        List<Cosign> allCosignsByUserIdReceiving = cosignRepository.findAllByUserIdReceiving(userIdReceiving);
        Map<Long, List<UserNameDTO>> mapOfPhrasesAndUserIdsIssuing = new HashMap<>();
        Map<Long, UserNameDTO> mapOfUserNameDTOs = new HashMap<>();

        for(Cosign cosign : allCosignsByUserIdReceiving) {
            Long userIdIssuing = cosign.getUserIdIssuing();
            Long phraseId = cosign.getPhraseId();

            if(!mapOfUserNameDTOs.containsKey(userIdIssuing)) {
                mapOfUserNameDTOs.put(userIdIssuing,userService.getUserNameDTO(userIdIssuing));
            }

            if(mapOfPhrasesAndUserIdsIssuing.containsKey(phraseId)){
                mapOfPhrasesAndUserIdsIssuing.get(phraseId).add(mapOfUserNameDTOs.get(userIdIssuing));
            } else {
                List<UserNameDTO> list = new ArrayList<>();
                list.add(mapOfUserNameDTOs.get(userIdIssuing));
                mapOfPhrasesAndUserIdsIssuing.put(phraseId,list);
            }
        }

        mapOfPhrasesAndUserIdsIssuing.forEach((k,v) -> {
            CosignsForUserDTO cosignsForUserDTO = CosignsForUserDTO.builder()
                    .phraseId(k)
                    .listOfCosigners(v)
                    .build();
            cosignsForUserDTOs.add(cosignsForUserDTO);
        });

        return cosignsForUserDTOs;
    }
}
