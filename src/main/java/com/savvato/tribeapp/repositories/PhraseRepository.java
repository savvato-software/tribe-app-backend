package com.savvato.tribeapp.repositories;


import com.savvato.tribeapp.entities.Phrase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhraseRepository extends CrudRepository<Phrase, Long> {

    @Query(nativeQuery = true, value = "SELECT p.*, COUNT(u.phrase_id) AS user_count FROM phrase p LEFT JOIN user_phrase u ON p.id = u.phrase_id GROUP BY p.id HAVING p.id = ?")
    Optional<Phrase> findPhraseByPhraseId(Long Id);

    Optional<Phrase> findByAdverbIdAndVerbIdAndPrepositionIdAndNounId(Long AdverbId, Long VerbId, Long PrepositionId, Long NounId);

}

