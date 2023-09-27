package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A request to change a password")
public class ChangePasswordRequest {

  @Schema(example = "123456")
  public String smsChallengeCode;

  @Schema(example = "1234567890")
  public String phoneNumber;

  @Schema(example = "admin", format = "password")
  public String pw;
}
