package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Adverb;
import com.savvato.tribeapp.entities.Preposition;
import com.savvato.tribeapp.entities.Verb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrepositionRepository extends CrudRepository<Preposition, Long> {
    Optional<Preposition> findByWord(String word);

    @Query(nativeQuery = true, value = "select word from preposition where id = ?")
    Optional<String> findPrepositionById(Long id);
}
