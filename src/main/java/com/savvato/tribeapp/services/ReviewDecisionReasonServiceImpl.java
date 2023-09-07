package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import com.savvato.tribeapp.entities.ReviewDecisionReason;
import com.savvato.tribeapp.repositories.ReviewDecisionReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewDecisionReasonServiceImpl implements ReviewDecisionReasonService {

    @Autowired
    ReviewDecisionReasonRepository reviewDecisionReasonRepository;

    @Override
    public Optional<List<ReviewDecisionReasonDTO>> getReviewDecisionReasons() {

        Optional<List<ReviewDecisionReason>> opt = reviewDecisionReasonRepository.findAllReviewDecisionReasons();

        List<ReviewDecisionReasonDTO> rdrDtoList = new ArrayList<>();

        if(opt.isPresent()){
            List<ReviewDecisionReason> rdrList = opt.get();

            for(ReviewDecisionReason rdr : rdrList) {
                ReviewDecisionReasonDTO rdrDto = ReviewDecisionReasonDTO.builder().build();
                rdrDto.reason = rdr.getReason();
                rdrDtoList.add(rdrDto);
            }
        }

        return Optional.of(rdrDtoList);
    }

}
