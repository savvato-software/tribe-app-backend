package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A request to connect two users")
public class ConnectRequest {
  @Schema(example = "1")
  public Long requestingUserId;

  @Schema(example = "1")
  public Long toBeConnectedWithUserId;

  @Schema(example = "ABCDEFGHIJKL")
  public String qrcodePhrase;
}
