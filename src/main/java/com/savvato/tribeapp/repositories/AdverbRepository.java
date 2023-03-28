package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Adverb;
import com.savvato.tribeapp.entities.Verb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdverbRepository extends CrudRepository<Adverb, Long> {
    Optional<Verb> findByWord(String word);
}
