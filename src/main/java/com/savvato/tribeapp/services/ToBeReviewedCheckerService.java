package com.savvato.tribeapp.services;

import com.google.gson.JsonObject;
import com.savvato.tribeapp.entities.ToBeReviewed;

import java.util.Optional;

public interface ToBeReviewedCheckerService {
    void updateUngroomedPhrases();
    boolean checkPartOfSpeech(String word, String expectedPartOfSpeech);

    boolean validatePhraseComponent(String word, String expectedPartOfSpeech);

    void validatePhrase(ToBeReviewed tbr);

    void updateTables(ToBeReviewed tbr);

    Optional<JsonObject> getWordDetails(String word);
}
