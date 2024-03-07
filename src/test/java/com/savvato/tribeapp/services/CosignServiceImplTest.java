package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.CosignsForUserDTO;
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
    public void testGetCosignersForUser(){
        // test data
        Long testUserIdIssuing = 1L;
        String testUserNameIssuing = "test";
        Long testUserIdReceiving = 2L;
        Long testPhraseId = 1L;

        // mock return data
        UserNameDTO mockUserNameDTO = UserNameDTO.builder()
                .userId(testUserIdIssuing)
                .userName(testUserNameIssuing)
                .build();

        List<Long> mockCosignerIds = new ArrayList<>();
        mockCosignerIds.add(testUserIdIssuing);

        // mock returns
        when(cosignRepository.findCosignersByUserIdReceivingAndPhraseId(anyLong(),anyLong())).thenReturn(mockCosignerIds);
        when(userService.getUserNameDTO(anyLong())).thenReturn(mockUserNameDTO);

        // expected results
        List<UserNameDTO> expectedListUserNameDTO = new ArrayList<>();
        expectedListUserNameDTO.add(mockUserNameDTO);

        // test
        List<UserNameDTO> userNameDTOS = cosignService.getCosignersForUserAttribute(testUserIdReceiving,testPhraseId);

        for(UserNameDTO user : userNameDTOS){
            assertEquals(user.userId, expectedListUserNameDTO.get(0).userId);
            assertEquals(user.userName,expectedListUserNameDTO.get(0).userName);
        }
    }

    @Test
    public void testGetAllCosignsForUser() {
        // test data
        Long testUserIdIssuing = 1L;
        String testUserNameIssuing = "test";
        Long testPhraseId = 1L;
        Long testUserIdReceiving = 2L;

        // mock return data
        Cosign mockCosign = new Cosign();
        mockCosign.setUserIdIssuing(testUserIdIssuing);
        mockCosign.setUserIdReceiving(testUserIdReceiving);
        mockCosign.setPhraseId(testPhraseId);

        List<Cosign> mockAllCosignsByUserIdReceivingList = new ArrayList<>();
        mockAllCosignsByUserIdReceivingList.add(mockCosign);

        UserNameDTO mockUserNameDTO = UserNameDTO.builder()
                .userId(testUserIdIssuing)
                .userName(testUserNameIssuing)
                .build();

        List<UserNameDTO> mockUserNameDTOSList = new ArrayList<>();
        mockUserNameDTOSList.add(mockUserNameDTO);

        CosignsForUserDTO mockCosignsForUserDTO = CosignsForUserDTO.builder()
                .phraseId(testPhraseId)
                .listOfCosigners(mockUserNameDTOSList)
                .build();

        // mock returns
        when(cosignRepository.findAllByUserIdReceiving(anyLong())).thenReturn(mockAllCosignsByUserIdReceivingList);
        when(userService.getUserNameDTO(anyLong())).thenReturn(mockUserNameDTO);

        // expected results
        List<CosignsForUserDTO> expectedCosignsForUserDTOSList = new ArrayList<>();
        expectedCosignsForUserDTOSList.add(mockCosignsForUserDTO);

        // test
        List<CosignsForUserDTO> testCosignsForUserDTOs = cosignService.getAllCosignsForUser(testUserIdReceiving);

        for(CosignsForUserDTO testCosignsForUserDTO : testCosignsForUserDTOs) {
            assertEquals(testCosignsForUserDTO.phraseId,expectedCosignsForUserDTOSList.get(0).phraseId);
            for(UserNameDTO testUserNameDTO : testCosignsForUserDTO.listOfCosigners){
                assertEquals(testUserNameDTO.userId, expectedCosignsForUserDTOSList.get(0).listOfCosigners.get(0).userId);
                assertEquals(testUserNameDTO.userName,expectedCosignsForUserDTOSList.get(0).listOfCosigners.get(0).userName);
            }
        }
    }
}
