package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "A Cosign DTO")
@Builder
public class CosignDTO {
    public Long userIdIssuing;
    public Long userIdReceiving;
    public Long phraseId;
}
