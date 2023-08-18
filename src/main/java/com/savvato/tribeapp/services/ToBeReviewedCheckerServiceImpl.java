package com.savvato.tribeapp.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.savvato.tribeapp.entities.RejectedPhrase;
import com.savvato.tribeapp.entities.ReviewSubmittingUser;
import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.RejectedPhraseRepository;
import com.savvato.tribeapp.repositories.ReviewSubmittingUserRepository;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.savvato.tribeapp.constants.Constants;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ToBeReviewedCheckerServiceImpl implements ToBeReviewedCheckerService {

    @Autowired
    ToBeReviewedRepository toBeReviewedRepository;
    @Autowired
    RejectedPhraseRepository rejectedPhraseRepository;
    @Autowired
    ReviewSubmittingUserRepository reviewSubmittingUserRepository;

    @Value("${WORDS_API_KEY}")
    private String apiKey;

    @Scheduled(fixedDelayString = "PT10M")
    @Override
    public void updateUngroomedPhrases() {
        log.info("from service: Beginning updateUngroomedPhrases process...");
        List<ToBeReviewed> ungroomedPhrases = toBeReviewedRepository.getAllUngroomed();
        for (ToBeReviewed tbr : ungroomedPhrases) {
            validatePhrase(tbr);
        }
    }
    @Override
    public Optional<JsonObject> getWordDetails(String word) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-RapidAPI-Key", apiKey);
        httpHeaders.set("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com");
        String url = "https://wordsapiv1.p.rapidapi.com/words/"+word;
        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = null;
        Optional responseJson = Optional.empty();
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (RestClientException e) {
            log.warn(word + " isn't an English word!");
            return responseJson;
        }

        responseJson = Optional.of(new JsonParser().parse(response.getBody()).getAsJsonObject());
        return responseJson;
    }

    @Override
    public boolean checkPartOfSpeech(String word, String expectedPartOfSpeech) {
        if(getWordDetails(word).isEmpty()){
            return false;
        } else {
            JsonObject wordDetails = getWordDetails(word).get();
            JsonArray definitions = wordDetails.getAsJsonArray("results");
            Set<String> partsOfSpeech = new HashSet<>();

            for (int i = 0; i < definitions.size(); i++) {
                JsonObject definition = definitions.get(i).getAsJsonObject();
                try {
                    partsOfSpeech.add(definition.get("partOfSpeech").getAsString());
                } catch (Exception e) {
                    // Words API may occasionally have a null parts of speech. This is an error on their part.
                    log.warn("Words API parts of speech null. Set for manual review.");
                    return true;
                }
            }
            return partsOfSpeech.contains(expectedPartOfSpeech);
        }
    }

    @Override
    public boolean validatePhraseComponent(String word, String expectedPartOfSpeech) {

        Boolean validPartOfSpeech = checkPartOfSpeech(word, expectedPartOfSpeech);

        if (validPartOfSpeech) {
            return true;
        } else {
            log.warn("The word passed in isn't a " + expectedPartOfSpeech + "!");
            return false;
        }

    }

    @Override
    public void validatePhrase(ToBeReviewed tbr) {

        boolean validPhrase = validatePhraseComponent(tbr.getNoun(), "noun")
                && validatePhraseComponent(tbr.getVerb(), "verb")
                && (tbr.getAdverb().equals(Constants.NULL_VALUE_WORD) || validatePhraseComponent(tbr.getAdverb(), "adverb"))
                && (tbr.getPreposition().equals(Constants.NULL_VALUE_WORD) || validatePhraseComponent(tbr.getPreposition(), "preposition"));

        if (validPhrase) {
            tbr.setHasBeenGroomed(true);
            toBeReviewedRepository.save(tbr);
        } else {
            log.warn("Phrase is invalid.");
            updateTables(tbr);
        }
    }

    @Override
    public void updateTables(ToBeReviewed tbr) {
        rejectedPhraseRepository.save(new RejectedPhrase(tbr.toString()));
        // TODO: Create notification for users when their submitted phrase has been rejected after review. Jira TRIB-153
        ReviewSubmittingUser rsu = new ReviewSubmittingUser(reviewSubmittingUserRepository.findUserIdByToBeReviewedId(tbr.getId()),tbr.getId());
        reviewSubmittingUserRepository.delete(rsu);
        toBeReviewedRepository.deleteById(tbr.getId());
    }

}
