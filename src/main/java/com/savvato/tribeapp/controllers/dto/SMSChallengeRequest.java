package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A request containing an SMS challenge's details")
public class SMSChallengeRequest {

  @Schema(example = "1234567890")
  public String phoneNumber;

  @Schema(example = "123456")
  public String code;
}
