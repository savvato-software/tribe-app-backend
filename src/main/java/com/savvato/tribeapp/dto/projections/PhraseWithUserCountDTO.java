package com.savvato.tribeapp.dto.projections;


import java.util.Objects;

public record PhraseWithUserCountDTO(Long id, Long adverbId, Long verbId, Long prepositionId, Long nounId,
                                     Long userCount) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhraseWithUserCountDTO phraseWithUserCountDTO = (PhraseWithUserCountDTO) o;
        return Objects.equals(id, phraseWithUserCountDTO.id) && Objects.equals(adverbId, phraseWithUserCountDTO.adverbId) && Objects.equals(verbId, phraseWithUserCountDTO.verbId) && Objects.equals(prepositionId, phraseWithUserCountDTO.prepositionId) && Objects.equals(nounId, phraseWithUserCountDTO.nounId);
    }
}
