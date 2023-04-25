package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.ReviewSubmittingUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewSubmittingUserRepository extends CrudRepository<ReviewSubmittingUser, Long> {
    @Query(nativeQuery = true, value="delete from review_submitting_user rsu where rsu.to_be_reviewed_id=?1")
    Long deleteByToBeReviewedId(Long toBeReviewedId);
    
}
