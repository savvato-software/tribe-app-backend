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
}

