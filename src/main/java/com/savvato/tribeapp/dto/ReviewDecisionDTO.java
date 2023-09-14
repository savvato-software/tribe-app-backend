package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class ReviewDecisionDTO {

    @Schema(example= "1", required=true)
    public Long reviewId;

    @Schema(example= "1", required=true)
    public Long userId;

    @Schema(example= "1", required=true)
    public Long reasonId;
}
