package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "A review decision DTO")
public class ReviewDecisionDTO {

  @Schema(example = "1")
  public Long reviewId;

  @Schema(example = "1")
  public Long userId;

  @Schema(example = "1")
  public Long reasonId;
}
