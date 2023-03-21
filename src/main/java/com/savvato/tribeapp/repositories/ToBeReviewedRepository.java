package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.ToBeReviewed;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToBeReviewedRepository extends CrudRepository<ToBeReviewed, Long> {
    @Query(nativeQuery = true, value = "select tbr.* from to_be_reviewed tbr where tbr.id>?1 and tbr.hasBeenGroomed=1")
    Optional<ToBeReviewed> findNextReviewEligible(Long id);
}