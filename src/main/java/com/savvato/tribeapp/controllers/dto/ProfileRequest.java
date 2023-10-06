package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A request containing a profile")
public class ProfileRequest {
  @Schema(example = "1")
  public Long id;

  @Schema(example = "1")
  public Long userId;

  @Schema(example = "John Doe")
  public String name;

  @Schema(example = "admin@tribeapp.com")
  public String email;

  @Schema(example = "1234567890")
  public String phone;
}
