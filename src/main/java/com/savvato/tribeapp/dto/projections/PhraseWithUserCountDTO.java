package com.savvato.tribeapp.dto.projections;


public record PhraseWithUserCountDTO(Long id, Long adverbId, Long verbId, Long prepositionId, Long nounId,
                                     Long userCount) {

}