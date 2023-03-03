package com.savvato.tribeapp.repositories;

import com.savvato.tribeapp.entities.ReviewDecision;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewDecisionRepository extends CrudRepository<ReviewDecision, Long> {

}
