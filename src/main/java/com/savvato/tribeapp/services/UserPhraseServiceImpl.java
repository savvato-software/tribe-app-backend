package com.savvato.tribeapp.services;

import com.savvato.tribeapp.repositories.UserPhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPhraseServiceImpl implements UserPhraseService{

    @Autowired
    UserPhraseRepository userPhraseRepository;

    @Override
    public Optional<List<Long>> findPhraseIdsByUserId(Long userId) {

        // access user phrase repo to get a list of all phrase ids associated with user
        Optional<List<Long>> optPhraseIdsForUser = userPhraseRepository.findPhraseIdsByUserId(userId);

        return optPhraseIdsForUser;

    }

}
