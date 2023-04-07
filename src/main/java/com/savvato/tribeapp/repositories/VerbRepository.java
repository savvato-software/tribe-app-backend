package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Verb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerbRepository extends CrudRepository<Verb, Long> {
    Optional<Verb> findByWord(String word);

    @Query(nativeQuery = true, value = "select word from verb where id = ?")
    Optional<String> findVerbById(Long id);
}
