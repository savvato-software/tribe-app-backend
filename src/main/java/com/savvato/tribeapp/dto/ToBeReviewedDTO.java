package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class ToBeReviewedDTO {

  @Schema(example = "1")
  public Long id;

  @Schema(implementation = Boolean.class, example = "false")
  public Boolean hasBeenGroomed;

  @Schema(example = "enthusiastically")
  public String adverb;

  @Schema(example = "volunteers")
  public String verb;

  @Schema(example = "at")
  public String preposition;

  @Schema(example = "UNICEF")
  public String noun;
}
