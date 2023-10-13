package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "A profile DTO")
public class ProfileDTO {
  @Schema(example = "John Doe")
  public String name;

  @Schema(example = "admin@tribeapp.com")
  public String email;

  @Schema(example = "1234567890")
  public String phone;

  @Schema(example = "2007-12-03T10:15:30")
  public String created;

  @Schema(example = "2007-12-13T10:15:30")
  public String lastUpdated;
}
