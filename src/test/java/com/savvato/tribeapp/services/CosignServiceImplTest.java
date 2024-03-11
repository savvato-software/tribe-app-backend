package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.entities.Cosign;
import com.savvato.tribeapp.repositories.CosignRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class CosignServiceImplTest extends AbstractServiceImplTest{

    @TestConfiguration
    static class CosignServiceImplTestContextConfiguration {

        @Bean
        public  CosignService cosignService() {
            return new CosignServiceImpl();
        }

    }

    @Autowired
    CosignService cosignService;

    @MockBean
    CosignRepository cosignRepository;

    @Test
    public void saveCosign() {
        Long userIdIssuing = 1L;
        Long userIdReceiving = 2L;
        Long phraseId = 1L;

        Cosign mockCosign = new Cosign();
        mockCosign.setUserIdIssuing(userIdIssuing);
        mockCosign.setUserIdReceiving(userIdReceiving);
        mockCosign.setPhraseId(phraseId);

        CosignDTO expectedCosignDTO = CosignDTO.builder().build();
        expectedCosignDTO.userIdIssuing = userIdIssuing;
        expectedCosignDTO.userIdReceiving = userIdReceiving;
        expectedCosignDTO.phraseId = phraseId;

        when(cosignRepository.save(Mockito.any())).thenReturn(mockCosign);

        Optional<CosignDTO> CosignDTO = cosignService.saveCosign(userIdIssuing, userIdReceiving, phraseId);

        verify(cosignRepository, times(1)).save(Mockito.any());
        assertThat(CosignDTO.get()).usingRecursiveComparison().isEqualTo(expectedCosignDTO);
    }

    @Test
    public void saveCosignAlreadyExisting() {
        Long userIdIssuing = 1L;
        Long userIdReceiving = 2L;
        Long phraseId = 1L;

        Cosign mockCosign = new Cosign();
        mockCosign.setUserIdIssuing(userIdIssuing);
        mockCosign.setUserIdReceiving(userIdReceiving);
        mockCosign.setPhraseId(phraseId);

        CosignDTO expectedCosignDTO = CosignDTO.builder().build();
        expectedCosignDTO.userIdIssuing = userIdIssuing;
        expectedCosignDTO.userIdReceiving = userIdReceiving;
        expectedCosignDTO.phraseId = phraseId;

        when(cosignRepository.save(Mockito.any())).thenReturn(mockCosign).thenReturn(mockCosign);

        Optional<CosignDTO> CosignDTO = cosignService.saveCosign(userIdIssuing, userIdReceiving, phraseId);

        assertThat(CosignDTO.get()).usingRecursiveComparison().isEqualTo(expectedCosignDTO);

        Optional<CosignDTO> CosignDTORepeat = cosignService.saveCosign(userIdIssuing, userIdReceiving, phraseId);

        assertThat(CosignDTORepeat.get()).usingRecursiveComparison().isEqualTo(expectedCosignDTO);
    }
}
