package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "An authorization request")
public class AuthRequest {

  @Schema(example = "admin@tribeapp.com")
  public String email;

  @Schema(example = "admin", format = "password")
  public String password;
}
