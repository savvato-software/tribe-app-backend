package com.savvato.tribeapp.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.entities.RejectedPhrase;
import com.savvato.tribeapp.entities.ReviewSubmittingUser;
import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.RejectedPhraseRepository;
import com.savvato.tribeapp.repositories.ReviewSubmittingUserRepository;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ToBeReviewedCheckerServiceImpl implements ToBeReviewedCheckerService {

    @Autowired
    ToBeReviewedRepository toBeReviewedRepository;
    @Autowired
    RejectedPhraseRepository rejectedPhraseRepository;
    @Autowired
    ReviewSubmittingUserRepository reviewSubmittingUserRepository;

    @Autowired
    RestTemplate restTemplate;
    @Value("${WORDS_API_KEY}")
    private String apiKey;

    @Generated
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-RapidAPI-Key", apiKey);
        httpHeaders.set("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com");
        String url = "https://wordsapiv1.p.rapidapi.com/words/" + word;
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
        Optional<JsonObject> wordDetails = getWordDetails(word);
        if (wordDetails.isEmpty()) {
            return false;
        } else {
            JsonArray definitions;
            Set<String> partsOfSpeech = new HashSet<>();

            try {
                definitions = wordDetails.get().getAsJsonArray("results");

                for (int i = 0; i < definitions.size(); i++) {
                    JsonObject definition = definitions.get(i).getAsJsonObject();
                    try {
                        partsOfSpeech.add(definition.get("partOfSpeech").getAsString());
                    } catch (UnsupportedOperationException e) {
                        // Words API may occasionally have a null parts of speech. This is an error on their part.
                        log.warn(word + " is missing a parts of speech definition from the Words API. Set for manual review.");
                        return true;
                    }
                }

            } catch (NullPointerException e) {
                // Words API may occasionally have a null results set. This is an error on their part.
                log.warn(word + " is missing a results set from the Words API. Set for manual review.");
                return true;
            }

            if (partsOfSpeech.contains(expectedPartOfSpeech)) {
                return true;
            } else {
                log.warn(word + " isn't a(n) " + expectedPartOfSpeech + "!");
                return false;
            }
        }
    }

    @Override
    public void validatePhrase(ToBeReviewed tbr) {

        boolean validPhrase = checkPartOfSpeech(tbr.getNoun(), "noun")
                && checkPartOfSpeech(tbr.getVerb(), "verb")
                && (tbr.getAdverb().equals(Constants.NULL_VALUE_WORD) || checkPartOfSpeech(tbr.getAdverb(), "adverb"))
                && (tbr.getPreposition().equals(Constants.NULL_VALUE_WORD) || checkPartOfSpeech(tbr.getPreposition(), "preposition"));

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
        ReviewSubmittingUser rsu = new ReviewSubmittingUser(reviewSubmittingUserRepository.findUserIdByToBeReviewedId(tbr.getId()), tbr.getId());
        reviewSubmittingUserRepository.delete(rsu);
        toBeReviewedRepository.deleteById(tbr.getId());
    }

}
