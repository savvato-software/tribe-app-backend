package com.savvato.tribeapp.repositories;


import com.savvato.tribeapp.dto.projections.PhraseWithUserCountDTO;
import com.savvato.tribeapp.entities.Phrase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhraseRepository extends CrudRepository<Phrase, Long> {

    @Query("""
            SELECT new com.savvato.tribeapp.dto.projections.PhraseWithUserCountDTO(p.id, p.adverbId, p.verbId, p.prepositionId, p.nounId, COUNT(u.id) AS userCount) FROM Phrase p LEFT JOIN p.users u GROUP BY p HAVING p.id = :id
            """)
    Optional<PhraseWithUserCountDTO> findPhraseByPhraseId(Long id);

    Optional<Phrase> findByAdverbIdAndVerbIdAndPrepositionIdAndNounId(Long AdverbId, Long VerbId, Long PrepositionId, Long NounId);

}

