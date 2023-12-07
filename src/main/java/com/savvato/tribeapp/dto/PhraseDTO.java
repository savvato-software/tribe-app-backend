package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "A phrase DTO")
public class PhraseDTO {

  @Schema(example = "1")
  public long id;

  @Schema(example = "enthusiastically")
  public String adverb;

  @Schema(example = "volunteers")
  public String verb;

  @Schema(example = "at")
  public String preposition;

  @Schema(example = "UNICEF")
  public String noun;
}
