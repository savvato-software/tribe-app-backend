package com.savvato.tribeapp.repositories;


import com.savvato.tribeapp.entities.Phrase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhraseRepository extends CrudRepository<Phrase, Long> {


}

