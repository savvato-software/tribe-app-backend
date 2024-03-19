package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "A Cosign DTO")
@Builder
public class CosignDTO {

    @Schema(example = "1")
    public Long userIdIssuing;

    @Schema(example = "2")
    public Long userIdReceiving;

    @Schema(example = "1")
    public Long phraseId;
}
