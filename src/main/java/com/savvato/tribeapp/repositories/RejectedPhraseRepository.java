package com.savvato.tribeapp.repositories;

import java.util.List;
import java.util.Optional;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface RejectedPhraseRepository extends CrudRepository<Phrase, Long> {

    @Query(nativeQuery = true, value = "select phrase.* from rejected_non_english_word phrase where phrase.id>?1 and phrase.hasBeenGroomed=1")
    Optional<Phrase> findNextAvailablePhrase(Long id);

}
