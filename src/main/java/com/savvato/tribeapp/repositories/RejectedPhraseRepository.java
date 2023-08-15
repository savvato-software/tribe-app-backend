package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.RejectedPhrase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RejectedPhraseRepository extends CrudRepository<RejectedPhrase, Long> {
    Optional<RejectedPhrase> findById(Long id);

    Optional<RejectedPhrase> findByRejectedPhrase(String rejectedPhrase);

}
