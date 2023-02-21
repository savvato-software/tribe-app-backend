package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.entities.UserRoleMap;
import org.springframework.data.repository.CrudRepository;

public interface PhraseRepository extends CrudRepository<Phrase, Long> {

}