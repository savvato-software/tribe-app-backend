package com.savvato.tribeapp.services;

import com.google.gson.JsonObject;
import com.savvato.tribeapp.entities.ToBeReviewed;

public interface ToBeReviewedCheckerService {
    void updateUngroomedPhrases();
    boolean checkPartOfSpeech(String word, String expectedPartOfSpeech, JsonObject wordDetails);

    boolean validatePhraseComponent(String word, String expectedPartOfSpeech);

    void validatePhrase(ToBeReviewed tbr);

    void updateTables(ToBeReviewed tbr);

    JsonObject getWordDetails(String word);
}
