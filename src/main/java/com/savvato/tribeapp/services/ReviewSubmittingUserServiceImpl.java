package com.savvato.tribeapp.services;
import com.savvato.tribeapp.dto.ToBeReviewedDTO;
import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.ReviewSubmittingUserRepository;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ReviewSubmittingUserServiceImpl implements
        ReviewSubmittingUserService {
    @Autowired
    ReviewSubmittingUserRepository reviewSubmittingUserRepository;
    @Autowired
    ToBeReviewedRepository toBeReviewedRepository;

    @Override
    public List<ToBeReviewedDTO> getUserPhrasesToBeReviewed(Long userId) {
        List<Long> toBeReviewedIds =
                reviewSubmittingUserRepository.findToBeReviewedIdByUserId(userId);
        if (toBeReviewedIds.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<ToBeReviewedDTO> toBeReviewedDTOS = new ArrayList<>();
            for (Long tbrId : toBeReviewedIds) {
                Optional<ToBeReviewed> tbr =
                        toBeReviewedRepository.findById(tbrId);
                ToBeReviewedDTO tbrDTO = ToBeReviewedDTO.builder().build();
                tbrDTO.id = tbr.get().getId();
                tbrDTO.hasBeenGroomed = tbr.get().isHasBeenGroomed();
                tbrDTO.adverb = tbr.get().getAdverb();
                tbrDTO.verb = tbr.get().getVerb();
                tbrDTO.preposition = tbr.get().getPreposition();
                tbrDTO.noun = tbr.get().getNoun();
                toBeReviewedDTOS.add(tbrDTO);
            }
            return toBeReviewedDTOS;
        }
    }
}