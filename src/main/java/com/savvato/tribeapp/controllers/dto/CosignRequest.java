package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A request containing a cosign's ids")
public class CosignRequest {
    @Schema(example = "1")
    public Long userIdIssuing;

    @Schema(example = "1")
    public Long userIdReceiving;

    @Schema(example = "1")
    public Long phraseId;
}
