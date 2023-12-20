package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A request to delete a connection between two users")
public class ConnectionDeleteRequest {
    @Schema(example = "1")
    public Long requestingUserId;

    @Schema(example = "2")
    public Long connectedWithUserId;
}