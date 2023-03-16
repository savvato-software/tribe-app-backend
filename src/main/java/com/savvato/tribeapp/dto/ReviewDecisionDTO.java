package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class ReviewDecisionDTO {
    public Long reviewId;
    public Long userId;
    public Long reasonId;
}
