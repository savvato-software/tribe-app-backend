package com.savvato.tribeapp.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.savvato.tribeapp.entities.RejectedPhrase;
import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.RejectedPhraseRepository;
import com.savvato.tribeapp.repositories.ReviewSubmittingUserRepository;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ToBeReviewedCheckerServiceImpl implements ToBeReviewedCheckerService {
    @Autowired
    ToBeReviewedRepository toBeReviewedRepository;
    @Autowired
    RejectedPhraseRepository rejectedPhraseRepository;
    @Autowired
    ReviewSubmittingUserRepository reviewSubmittingUserRepository;
    static final Logger LOGGER = Logger.getLogger(ToBeReviewedChecker.class.getName());
    @Value("${WORDS_API_KEY}")
    private String apiKey;
    @Override
    public void updateUngroomedPhrases() {
        LOGGER.info("Beginning updateUngroomedPhrases process...");
        List<ToBeReviewed> ungroomedPhrases = toBeReviewedRepository.getAllUngroomed();
        for (ToBeReviewed tbr : ungroomedPhrases) {
            validatePhrase(tbr);
        }
    }
    @Override
    public JsonObject getWordDetails(String word) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-RapidAPI-Key", apiKey);
        httpHeaders.set("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com");
        String url = "https://wordsapiv1.p.rapidapi.com/words/"+word;
        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();

        JsonObject responseJson = new JsonParser().parse(response.getBody()).getAsJsonObject();
        return responseJson;
    }
    @Override
    public boolean checkPartOfSpeech(String word, String expectedPartOfSpeech, JsonObject wordDetails) {
        JsonArray definitions = wordDetails.getAsJsonArray("results");
        Set<String> partsOfSpeech = new HashSet<>();

        for (int i = 0; i < definitions.size(); i++) {
            JsonObject definition = definitions.get(i).getAsJsonObject();
            partsOfSpeech.add(definition.get("partOfSpeech").getAsString());
        }

        return partsOfSpeech.contains(expectedPartOfSpeech);
    }
    @Override
    public boolean validatePhraseComponent(String word, String expectedPartOfSpeech) {
        if ((expectedPartOfSpeech == "noun" || expectedPartOfSpeech == "verb") && word.length() == 0) {
            Exception e = new IllegalStateException();
            LOGGER.log(Level.SEVERE, "Critical error: expected component " + expectedPartOfSpeech + " is empty!", e);
            return false;
        }
        Boolean validPartOfSpeech = checkPartOfSpeech(word, expectedPartOfSpeech, getWordDetails(word));
        try {
            if (validPartOfSpeech) {
                return true;
            } else {
                LOGGER.warning("The word passed in isn't a " + expectedPartOfSpeech + "!");
                return false;
            }
        } catch (Exception e) {
            LOGGER.warning("The " + expectedPartOfSpeech + " passed in isn't an English word!");
            return false;
        }
    }

    @Override
    public void validatePhrase(ToBeReviewed tbr) {
        Optional<RejectedPhrase> matchingRejectedPhrase = rejectedPhraseRepository.findByRejectedPhrase(tbr.toString());
        if (matchingRejectedPhrase.isEmpty()) {
            boolean validPhrase = validatePhraseComponent(tbr.getNoun(), "noun") && validatePhraseComponent(tbr.getVerb(), "verb") && validatePhraseComponent(tbr.getAdverb(), "adverb") && validatePhraseComponent(tbr.getPreposition(), "preposition");
            if (validPhrase) {
                updateTables(tbr, false, true);
            } else {
                LOGGER.warning("Phrase is invalid.");
                updateTables(tbr, false, false);
            }
        }
        else {
            LOGGER.warning("Phrase has already been rejected!");
            updateTables(tbr, true, false);
        }
    }

    @Override
    public void updateTables(ToBeReviewed tbr, boolean hasMatchingRejectedPhrase, boolean phraseValid) {
        if (phraseValid) {
            toBeReviewedRepository.setHasBeenGroomedTrue(tbr.getId());
        }
        else {
            if (!hasMatchingRejectedPhrase) {
                RejectedPhrase rp = new RejectedPhrase();
                rp.setRejectedPhrase(tbr.toString());
                rejectedPhraseRepository.save(rp);
            }
            reviewSubmittingUserRepository.deleteByToBeReviewedId(tbr.getId());
            toBeReviewedRepository.deleteById(tbr.getId());
        }
    }

}
