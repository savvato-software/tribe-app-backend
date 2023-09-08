package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import com.savvato.tribeapp.entities.ReviewDecisionReason;
import com.savvato.tribeapp.repositories.ReviewDecisionReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewDecisionReasonServiceImpl implements ReviewDecisionReasonService {

    @Autowired
    ReviewDecisionReasonRepository reviewDecisionReasonRepository;

    @Override
    public List<ReviewDecisionReasonDTO> getReviewDecisionReasons() {

        List<ReviewDecisionReason> rdrList = reviewDecisionReasonRepository.findAllReviewDecisionReasons();
        List<ReviewDecisionReasonDTO> rdrDtoList = new ArrayList<>();

        if(rdrList.isEmpty()){
            throw new IllegalStateException("Unable to get results from review_decision_reasons table in the database.");
        } else {
            for(ReviewDecisionReason rdr : rdrList) {
                ReviewDecisionReasonDTO rdrDto = ReviewDecisionReasonDTO.builder().build();
                rdrDto.id = rdr.getId();
                rdrDto.reason = rdr.getReason();
                rdrDtoList.add(rdrDto);
            }
        }

        return rdrDtoList;
    }

}
