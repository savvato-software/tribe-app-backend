package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ToBeReviewedServiceImpl implements ToBeReviewedService {
    @Autowired
    ToBeReviewedRepository toBeReviewedRepository;

    @Override
    public Long getLastAssignedForReview() {
        return lastAssignedForReviewId;
    }
    public void setLastAssignedForReview(Long id) {
        this.lastAssignedForReviewId = id;
    }
    Long lastAssignedForReviewId = 0L;
    public Optional<ToBeReviewed> getReviewPhrase() {
        Optional<ToBeReviewed> opt = toBeReviewedRepository.findNextReviewEligible(lastAssignedForReviewId);
        if (opt.isPresent()) {
            setLastAssignedForReview(opt.get().getId());
            return opt;
        }
        return Optional.empty();
    }



}
