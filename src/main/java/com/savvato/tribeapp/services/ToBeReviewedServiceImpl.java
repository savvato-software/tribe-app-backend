package com.savvato.tribeapp.services;

import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.dto.ToBeReviewedDTO;
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
    public Optional<ToBeReviewedDTO> getReviewPhrase() {
        Optional<ToBeReviewed> opt = toBeReviewedRepository.findNextReviewEligible(lastAssignedForReviewId);
        if (opt.isPresent()) {
            ToBeReviewed tbr = opt.get();
            if(tbr.getAdverb().equals(Constants.NULL_VALUE_WORD)){
                tbr.setAdverb("");
            }
            if(tbr.getPreposition().equals(Constants.NULL_VALUE_WORD)){
                tbr.setPreposition("");
            }
            setLastAssignedForReview(tbr.getId());

            ToBeReviewedDTO rtn = ToBeReviewedDTO.builder()
                    .toBeReviewedId(tbr.getId())
                    .hasBeenGroomed(tbr.isHasBeenGroomed())
                    .adverb(tbr.getAdverb())
                    .verb(tbr.getVerb())
                    .preposition(tbr.getPreposition())
                    .noun(tbr.getNoun())
                    .build();

            return Optional.of(rtn);
        }
        return Optional.empty();
    }
}
