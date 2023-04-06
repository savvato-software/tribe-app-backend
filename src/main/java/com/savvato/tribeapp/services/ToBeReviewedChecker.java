package com.savvato.tribeapp.services;


import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ToBeReviewedChecker {
    static final Logger LOGGER = Logger.getLogger(ToBeReviewedChecker.class.getName());

    @Scheduled(fixedDelayString = "PT10M")
    public void updateUngroomedPhrases() {
        LOGGER.info("Beginning updateUngroomedPhrases process...");

    }

}
