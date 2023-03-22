package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.repositories.PhraseRepository;
import com.savvato.tribeapp.repositories.RejectedNonEnglishWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttributesServiceImpl implements AttributesService{

    // controller - to call this service
    // this service to take the four words
    // query the non-english word table for each of the four words
    // should return false if it appears in the table
    // validation should not pass
    // am I returning a boolean? - so yes I am!!!

    // method or two -
    // method - takes the four words and that method calls another method that takes a single word
    // and call that single word method for each of the four words
    // the one word function will call the repository to see if it appears in the table

    RejectedNonEnglishWordRepository rejectedNonEnglishWordRepository;

    // method - takes the four words and that method calls another method that takes a single word
    public boolean isFourWordsValid(String verb, String noun, String adverb, String preposition) {

        boolean rtn = true;
        rtn = rtn && checkSingleWord(verb);
        rtn = rtn && checkSingleWord(noun);
        rtn = rtn && checkSingleWord(adverb);
        rtn = rtn && checkSingleWord(preposition);

        System.out.println("take four words method reached");
        return rtn;
    }

    public boolean checkSingleWord(String word) {

        Optional<Phrase>opt = this.rejectedNonEnglishWordRepository.findPhraseByGivenWord(word);

        if( opt.isPresent() ) {
            return false;
        }
        else
            return true;
    }


//    @Autowired
//    //RejectedPhraseRepository rejectedPhraseRepository;
//
//    @Override
//    //public Long getLastAssignedPhraseId() {
//        return lastAssignedPhraseId;
//    }
//
//    public Optional<Phrase> getRejectedPhrase() {
//        Optional<Phrase> opt = comparePhrase.findNextAvailablePhrase(lastAssignedPhraseId);
//        if (opt.isPresent()) {
//            setLastAssignedPhraseId(opt.get().getId());
//            return opt;
//        }
//        return Optional.empty();
//    }

}
