package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.Adverb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdverbRepository extends CrudRepository<Adverb, Long> {
    Optional<Adverb> findByWord(String word);

    @Query(nativeQuery = true, value = "select word from adverb where id = ?")
    Optional<String> findAdverbById(Long id);
}
