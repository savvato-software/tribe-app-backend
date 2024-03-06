package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.UserNameDTO;
import com.savvato.tribeapp.entities.Cosign;
import com.savvato.tribeapp.repositories.CosignRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.any;
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

    @MockBean
    UserService userService;

    @Test
    public void saveCosign() {
        Long userIdIssuing = 1L;
        Long userIdReceiving = 1L;
        Long phraseId = 1L;

        Cosign cosign = new Cosign();
        cosign.setUserIdIssuing(userIdIssuing);
        cosign.setUserIdReceiving(userIdReceiving);
        cosign.setPhraseId(phraseId);

        CosignDTO cosignDTO = CosignDTO.builder().build();
        cosignDTO.userIdIssuing = userIdIssuing;
        cosignDTO.userIdReceiving = userIdReceiving;
        cosignDTO.phraseId = phraseId;

        when(cosignRepository.save(Mockito.any())).thenReturn(cosign);

        CosignDTO expectedCosignDTO = cosignService.saveCosign(userIdIssuing, userIdReceiving, phraseId);

        verify(cosignRepository, times(1)).save(Mockito.any());
        assertEquals(cosignDTO.userIdIssuing, expectedCosignDTO.userIdIssuing);
        assertEquals(cosignDTO.userIdReceiving, expectedCosignDTO.userIdReceiving);
        assertEquals(cosignDTO.phraseId, expectedCosignDTO.phraseId);
    }

    @Test
    public void saveCosignAlreadyExisting() {
        Long userIdIssuing = 1L;
        Long userIdReceiving = 1L;
        Long phraseId = 1L;

        Cosign cosign = new Cosign();
        cosign.setUserIdIssuing(userIdIssuing);
        cosign.setUserIdReceiving(userIdReceiving);
        cosign.setPhraseId(phraseId);

        CosignDTO cosignDTO = CosignDTO.builder().build();
        cosignDTO.userIdIssuing = userIdIssuing;
        cosignDTO.userIdReceiving = userIdReceiving;
        cosignDTO.phraseId = phraseId;

        when(cosignRepository.save(Mockito.any())).thenReturn(cosign).thenReturn(cosign);

        CosignDTO expectedCosignDTO = cosignService.saveCosign(userIdIssuing, userIdReceiving, phraseId);

        assertEquals(cosignDTO.userIdIssuing, expectedCosignDTO.userIdIssuing);
        assertEquals(cosignDTO.userIdReceiving, expectedCosignDTO.userIdReceiving);
        assertEquals(cosignDTO.phraseId, expectedCosignDTO.phraseId);

        CosignDTO expectedCosignDTORepeat = cosignService.saveCosign(userIdIssuing, userIdReceiving, phraseId);

        assertEquals(cosignDTO.userIdIssuing, expectedCosignDTORepeat.userIdIssuing);
        assertEquals(cosignDTO.userIdReceiving, expectedCosignDTORepeat.userIdReceiving);
        assertEquals(cosignDTO.phraseId, expectedCosignDTORepeat.phraseId);
    }

    @Test
    public void testGetCosignersForUserAttributeHappyPath(){
        // test data
        Long testUserIdIssuing = 1L;
        String testUserNameIssuing = "test";
        Long testUserIdReceiving = 2L;
        Long testPhraseId = 1L;

        // mock return data
        UserNameDTO userNameDTO = UserNameDTO.builder()
                .userId(testUserIdIssuing)
                .userName(testUserNameIssuing)
                .build();

        List<Long> expectedCosignerIds = new ArrayList<>();
        expectedCosignerIds.add(testUserIdIssuing);

        List<UserNameDTO> expectedListUserNameDTO = new ArrayList<>();
        expectedListUserNameDTO.add(userNameDTO);

        // mock returns
        when(cosignRepository.findCosignersByUserIdReceivingAndPhraseId(anyLong(),anyLong())).thenReturn(expectedCosignerIds);

        when(userService.getUserNameDTO(anyLong())).thenReturn(userNameDTO);

        // test
        List<UserNameDTO> userNameDTOS = cosignService.getCosignersForUserAttribute(testUserIdReceiving,testPhraseId);

        for(UserNameDTO user : userNameDTOS){
            assertEquals(user.userId, expectedListUserNameDTO.get(0).userId);
            assertEquals(user.userName,expectedListUserNameDTO.get(0).userName);
        }
    }
}
