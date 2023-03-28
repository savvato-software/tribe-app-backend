package com.savvato.tribeapp.services;

public interface AttributesService {

    boolean isPhraseValid(String verb, String noun, String adverb, String preposition);
    void applyPhraseToUser(String verb, String noun, String adverb, String preposition);

}

