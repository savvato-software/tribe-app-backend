package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.RejectedPhrase;
import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.RejectedPhraseRepository;
import com.savvato.tribeapp.repositories.ReviewSubmittingUserRepository;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ToBeReviewedChecker {
    @Autowired
    ToBeReviewedRepository toBeReviewedRepository;
    @Autowired
    RejectedPhraseRepository rejectedPhraseRepository;

    @Autowired
    ReviewSubmittingUserRepository reviewSubmittingUserRepository;

    @Scheduled(fixedDelayString = "PT10M")
    public void updateUngroomedPhrases() {
        log.info("Beginning updateUngroomedPhrases process...");
        List<ToBeReviewed> ungroomedPhrases = toBeReviewedRepository.getAllUngroomed();

        for (ToBeReviewed tbr : ungroomedPhrases) {
            Optional<RejectedPhrase> matchingRejectedPhrase = rejectedPhraseRepository.findById(tbr.getId());
            if (matchingRejectedPhrase.isEmpty()) {
                // contact wordsApi...
            }
            else {
                log.error("Phrase has already been rejected!");
                reviewSubmittingUserRepository.deleteByToBeReviewedId(tbr.getId());
                toBeReviewedRepository.deleteById(tbr.getId());
            }
        }
    }

}
