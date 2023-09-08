package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.ReviewDecisionReason;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewDecisionReasonRepository extends CrudRepository<ReviewDecisionReason, Long> {

    @Query(nativeQuery = true, value = "select rdr.* from review_decision_reason rdr")
    List<ReviewDecisionReason> findAllReviewDecisionReasons();

}
