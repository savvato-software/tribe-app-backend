package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class ToBeReviewedDTO {

    public Boolean hasBeenGroomed;
    public String adverb;
    public String verb;
    public String preposition;
    public String noun;

}