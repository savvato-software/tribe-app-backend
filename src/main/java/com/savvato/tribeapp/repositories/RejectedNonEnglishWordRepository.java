package com.savvato.tribeapp.repositories;

import java.util.Optional;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.entities.RejectedNonEnglishWord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RejectedNonEnglishWordRepository extends CrudRepository<RejectedNonEnglishWord, Long> {

    @Query(nativeQuery = true, value = "select rnew.* from rejected_non_english_word rnew where rnew.word=?1")
    Optional<Phrase> findPhraseByGivenWord(String word);

    // close but no cigar for the select statement
    // think about what you want this to return
    // what query would return the data you are looking for?

    // hasBeenGroomed - column in that table, not necessarily this table


    // interface uses the entity as a type
    // queries the db, handles the db access for you
    // entity way of describing the table
    // getting information from the table - repository - query, ask, do any db command with that table

    // service is the business logic location
    // api's
    // url for your controller
    // attributes is your domain object
    // controller deals with that domain object
    // controller talks to services
    // service also deals with that specific domain object
    // api/attributes - business logic is dealing with the db
    // db is dealing with the data around this domain object attributes
    // request from front end all the way to the backend
    // go through controller - services - repositories - entities


}
