package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class PhraseDTO {

    @Schema(example= "enthusiastically", required=true)
    public String adverb;
    @Schema(example= "volunteers", required=true)
    public String verb;
    @Schema(example= "at", required=true)
    public String preposition;
    @Schema(example= "UNICEF", required=true)
    public String noun;

}
