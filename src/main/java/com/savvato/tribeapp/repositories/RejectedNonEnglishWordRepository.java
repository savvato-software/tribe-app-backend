package com.savvato.tribeapp.repositories;

import java.util.Optional;

import com.savvato.tribeapp.entities.RejectedNonEnglishWord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RejectedNonEnglishWordRepository extends CrudRepository<RejectedNonEnglishWord, Long> {

    @Query(nativeQuery = true, value = "select rnew.* from rejected_non_english_word rnew where rnew.word=?1")
    Optional<RejectedNonEnglishWord> findByWord(String word);

}
