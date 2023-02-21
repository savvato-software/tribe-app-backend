package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRoleMap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhraseRepository extends CrudRepository<Phrase, Long> {
    @Query(nativeQuery = true, value = "select phrase.* from to_be_reviewed phrase where phrase.id>?1 and phrase.hasBeenGroomed=1")
    Optional<Phrase> findNextAvailablePhrase(Long id);
}