package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class UserNameDTO {
  @Schema(example = "1")
  public Long userId;

  @Schema(example = "John Doe")
  public String userName;

}
