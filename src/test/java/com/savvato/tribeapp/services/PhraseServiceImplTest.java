package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.entities.UserRoleMap;
import com.savvato.tribeapp.repositories.PhraseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class PhraseServiceImplTest extends AbstractServiceImplTest {

    @TestConfiguration
    static class PhraseServiceTestContextConfiguration {
        @Bean
        public PhraseService phraseService() {
            return new PhraseServiceImpl();
        }
    }
    @Autowired
    PhraseService phraseService;

    @MockBean
    PhraseRepository phraseRepository;

    // test that the method is returning the database's results correctly
    @Test
    public void getReviewPhrase() {
        Phrase expectedPhrase = new Phrase();
        expectedPhrase.setId(1L);
        expectedPhrase.setHasBeenGroomed(true);
        expectedPhrase.setAdverb("competitively");
        expectedPhrase.setVerb("programs");
        expectedPhrase.setPreposition("with");
        expectedPhrase.setNoun("Python");

        Mockito.when(phraseRepository.findNextAvailablePhrase(any(Long.class))).thenReturn(Optional.of(expectedPhrase));

        Optional<Phrase> rtn = phraseService.getReviewPhrase();

        assertEquals(rtn.get(), expectedPhrase);
        assertEquals(phraseService.getLastAssignedPhraseId(), expectedPhrase.getId());
    }

    @Test
    public void setLastAssignedPhraseId(Long id) {
        Phrase expectedPhrase = new Phrase();
        expectedPhrase.setId(1L);
        expectedPhrase.setHasBeenGroomed(true);
        expectedPhrase.setAdverb("competitively");
        expectedPhrase.setVerb("programs");
        expectedPhrase.setPreposition("with");
        expectedPhrase.setNoun("Python");

        Mockito.when(phraseRepository.findNextAvailablePhrase(any(Long.class))).thenReturn(Optional.of(expectedPhrase));
        Mockito.when(phraseService.getReviewPhrase());
    }
}
