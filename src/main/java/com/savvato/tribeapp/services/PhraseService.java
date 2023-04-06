package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.PhraseDTO;

import java.util.List;
import java.util.Optional;

public interface PhraseService {

    Optional<List<PhraseDTO>> getListOfPhraseDTOByUserId(Long id);
}
