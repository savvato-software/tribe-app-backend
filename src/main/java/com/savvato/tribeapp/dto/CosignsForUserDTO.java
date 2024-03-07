package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "All Cosigns for user DTO")
@Builder
public class CosignsForUserDTO {

    @Schema(example = "1")
    public long phraseId;

    @Schema(example = "[{\"userId\": 1, \"userName\": \"testuser\"}]")
    public List<UsernameDTO> listOfCosigners;
}
