package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.ToBeReviewedDTO;

import java.util.List;

public interface ReviewSubmittingUserService {
    List<ToBeReviewedDTO> getUserPhrasesToBeReviewed(Long userId);
}
