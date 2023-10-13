package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "A review decision reason DTO")
public class ReviewDecisionReasonDTO {
  @Schema(example = "1")
  public Long id;

  @Schema(example = "approved")
  public String reason;
}
