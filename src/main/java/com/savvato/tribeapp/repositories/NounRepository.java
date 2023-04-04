package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Noun;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NounRepository extends CrudRepository<Noun, Long> {
    Optional<Noun> findByWord(String word);

    @Query(nativeQuery = true, value = "select word from noun where id = ?")
    Optional<String> findNounById(Long id);
}
