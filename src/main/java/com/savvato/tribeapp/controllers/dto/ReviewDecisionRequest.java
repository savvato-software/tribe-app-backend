package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A request containing a review decision")
public class ReviewDecisionRequest {
  @Schema(example = "1")
  public Long reviewId;

  @Schema(example = "1")
  public Long userId;

  @Schema(example = "1")
  public Long reasonId;
}
