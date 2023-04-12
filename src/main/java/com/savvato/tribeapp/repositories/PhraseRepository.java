package com.savvato.tribeapp.repositories;


import com.savvato.tribeapp.entities.Phrase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhraseRepository extends CrudRepository<Phrase, Long> {

    @Query(nativeQuery = true, value = "select * from phrase where id = ?")
    Optional<Phrase> findPhraseByPhraseId(Long Id);

    @Query(nativeQuery = true, value = "select p.id from phrase p where p.adverb_id = ? and p.verb_id = ? and p.preposition_id = ? and p.noun_id = nounId")
    Optional<Long> findPhraseIdByAdverbIdAndVerbIdAndPrepositionIdAndNounId(Long AdverbId, Long VerbId, Long PrepositionId, Long NounId);

    //Optional<Long> findIdByAdverbId(Long AdverbId);
}

