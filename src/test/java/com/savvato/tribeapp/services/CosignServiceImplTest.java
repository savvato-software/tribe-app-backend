package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.CosignsForUserDTO;
import com.savvato.tribeapp.dto.UsernameDTO;
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

import java.util.ArrayList;
import java.util.List;

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
        String testUsernameIssuing = "test";
        Long testUserIdReceiving = 2L;
        Long testPhraseId = 1L;

        // mock return data
        UsernameDTO mockUsernameDTO = UsernameDTO.builder()
                .userId(testUserIdIssuing)
                .username(testUsernameIssuing)
                .build();

        List<Long> mockCosignerIds = new ArrayList<>();
        mockCosignerIds.add(testUserIdIssuing);

        // mock returns
        when(cosignRepository.findCosignersByUserIdReceivingAndPhraseId(anyLong(),anyLong())).thenReturn(mockCosignerIds);
        when(userService.getUsernameDTO(anyLong())).thenReturn(mockUsernameDTO);

        // expected results
        List<UsernameDTO> expectedListUsernameDTO = new ArrayList<>();
        expectedListUsernameDTO.add(mockUsernameDTO);

        // test
        List<UsernameDTO> usernameDTOS = cosignService.getCosignersForUserAttribute(testUserIdReceiving,testPhraseId);

        for(UsernameDTO user : usernameDTOS){
            assertEquals(user.userId, expectedListUsernameDTO.get(0).userId);
            assertEquals(user.username, expectedListUsernameDTO.get(0).username);
        }
    }

    @Test
    public void testGetCosignersForUserWithThreeCosigners(){
        // test data
        Long testUserIdIssuing1 = 1L;
        Long testUserIdIssuing2 = 2L;
        Long testUserIdIssuing3 = 3L;
        String testUsernameIssuing1 = "test1";
        String testUsernameIssuing2 = "test2";
        String testUsernameIssuing3 = "test3";
        Long testUserIdReceiving = 4L;
        Long testPhraseId = 1L;

        // mock return data
        UsernameDTO mockUsernameDTO1 = UsernameDTO.builder()
                .userId(testUserIdIssuing1)
                .username(testUsernameIssuing1)
                .build();

        UsernameDTO mockUsernameDTO2 = UsernameDTO.builder()
                .userId(testUserIdIssuing2)
                .username(testUsernameIssuing2)
                .build();

        UsernameDTO mockUsernameDTO3 = UsernameDTO.builder()
                .userId(testUserIdIssuing3)
                .username(testUsernameIssuing3)
                .build();

        List<Long> mockCosignerIds = new ArrayList<>();
        mockCosignerIds.add(testUserIdIssuing1);
        mockCosignerIds.add(testUserIdIssuing2);
        mockCosignerIds.add(testUserIdIssuing3);

        // mock returns
        when(cosignRepository.findCosignersByUserIdReceivingAndPhraseId(anyLong(),anyLong())).thenReturn(mockCosignerIds);
        when(userService.getUsernameDTO(anyLong())).thenReturn(mockUsernameDTO1).thenReturn(mockUsernameDTO2).thenReturn(mockUsernameDTO3);

        // expected results
        List<UsernameDTO> expectedListUsernameDTO = new ArrayList<>();
        expectedListUsernameDTO.add(mockUsernameDTO1);
        expectedListUsernameDTO.add(mockUsernameDTO2);
        expectedListUsernameDTO.add(mockUsernameDTO3);

        // test
        List<UsernameDTO> usernameDTOS = cosignService.getCosignersForUserAttribute(testUserIdReceiving,testPhraseId);

        for(int i=0; i<usernameDTOS.size(); i++){
            assertEquals(usernameDTOS.get(i).userId, expectedListUsernameDTO.get(i).userId);
            assertEquals(usernameDTOS.get(i).username, expectedListUsernameDTO.get(i).username);
        }
    }

    @Test
    public void testGetAllCosignsForUser() {
        // test data
        Long testUserIdIssuing = 1L;
        String testUsernameIssuing = "test";
        Long testPhraseId = 1L;
        Long testUserIdReceiving = 2L;

        // mock return data
        Cosign mockCosign = new Cosign();
        mockCosign.setUserIdIssuing(testUserIdIssuing);
        mockCosign.setUserIdReceiving(testUserIdReceiving);
        mockCosign.setPhraseId(testPhraseId);

        List<Cosign> mockAllCosignsByUserIdReceivingList = new ArrayList<>();
        mockAllCosignsByUserIdReceivingList.add(mockCosign);

        UsernameDTO mockUsernameDTO = UsernameDTO.builder()
                .userId(testUserIdIssuing)
                .username(testUsernameIssuing)
                .build();

        List<UsernameDTO> mockUsernameDTOSList = new ArrayList<>();
        mockUsernameDTOSList.add(mockUsernameDTO);

        CosignsForUserDTO mockCosignsForUserDTO = CosignsForUserDTO.builder()
                .phraseId(testPhraseId)
                .listOfCosigners(mockUsernameDTOSList)
                .build();

        // mock returns
        when(cosignRepository.findAllByUserIdReceiving(anyLong())).thenReturn(mockAllCosignsByUserIdReceivingList);
        when(userService.getUsernameDTO(anyLong())).thenReturn(mockUsernameDTO);

        // expected results
        List<CosignsForUserDTO> expectedCosignsForUserDTOSList = new ArrayList<>();
        expectedCosignsForUserDTOSList.add(mockCosignsForUserDTO);

        // test
        List<CosignsForUserDTO> testCosignsForUserDTOs = cosignService.getAllCosignsForUser(testUserIdReceiving);

        for(CosignsForUserDTO testCosignsForUserDTO : testCosignsForUserDTOs) {
            assertEquals(testCosignsForUserDTO.phraseId,expectedCosignsForUserDTOSList.get(0).phraseId);
            for(UsernameDTO testUsernameDTO : testCosignsForUserDTO.listOfCosigners){
                assertEquals(testUsernameDTO.userId, expectedCosignsForUserDTOSList.get(0).listOfCosigners.get(0).userId);
                assertEquals(testUsernameDTO.username,expectedCosignsForUserDTOSList.get(0).listOfCosigners.get(0).username);
            }
        }
    }

    @Test
    public void testGetAllCosignsForUserWithThreeCosigns() {
        // test data
        Long testUserIdIssuing1 = 1L;
        Long testUserIdIssuing2 = 2L;
        Long testUserIdIssuing3 = 3L;
        String testUsernameIssuing1 = "test1";
        String testUsernameIssuing2 = "test2";
        String testUsernameIssuing3 = "test3";
        Long testPhraseId1 = 1L;
        Long testPhraseId2 = 2L;
        Long testPhraseId3 = 3L;
        Long testUserIdReceiving = 4L;

        // mock return data
        Cosign mockCosign1a = new Cosign();
        mockCosign1a.setUserIdIssuing(testUserIdIssuing1);
        mockCosign1a.setUserIdReceiving(testUserIdReceiving);
        mockCosign1a.setPhraseId(testPhraseId1);

        Cosign mockCosign1b = new Cosign();
        mockCosign1b.setUserIdIssuing(testUserIdIssuing2);
        mockCosign1b.setUserIdReceiving(testUserIdReceiving);
        mockCosign1b.setPhraseId(testPhraseId1);

        Cosign mockCosign1c = new Cosign();
        mockCosign1c.setUserIdIssuing(testUserIdIssuing3);
        mockCosign1c.setUserIdReceiving(testUserIdReceiving);
        mockCosign1c.setPhraseId(testPhraseId1);


        Cosign mockCosign2a = new Cosign();
        mockCosign2a.setUserIdIssuing(testUserIdIssuing1);
        mockCosign2a.setUserIdReceiving(testUserIdReceiving);
        mockCosign2a.setPhraseId(testPhraseId2);

        Cosign mockCosign2b = new Cosign();
        mockCosign2b.setUserIdIssuing(testUserIdIssuing2);
        mockCosign2b.setUserIdReceiving(testUserIdReceiving);
        mockCosign2b.setPhraseId(testPhraseId2);

        Cosign mockCosign2c = new Cosign();
        mockCosign2c.setUserIdIssuing(testUserIdIssuing3);
        mockCosign2c.setUserIdReceiving(testUserIdReceiving);
        mockCosign2c.setPhraseId(testPhraseId2);


        Cosign mockCosign3a = new Cosign();
        mockCosign3a.setUserIdIssuing(testUserIdIssuing1);
        mockCosign3a.setUserIdReceiving(testUserIdReceiving);
        mockCosign3a.setPhraseId(testPhraseId3);

        Cosign mockCosign3b = new Cosign();
        mockCosign3b.setUserIdIssuing(testUserIdIssuing2);
        mockCosign3b.setUserIdReceiving(testUserIdReceiving);
        mockCosign3b.setPhraseId(testPhraseId3);

        Cosign mockCosign3c = new Cosign();
        mockCosign3c.setUserIdIssuing(testUserIdIssuing3);
        mockCosign3c.setUserIdReceiving(testUserIdReceiving);
        mockCosign3c.setPhraseId(testPhraseId3);


        List<Cosign> mockAllCosignsByUserIdReceivingList = new ArrayList<>();
        mockAllCosignsByUserIdReceivingList.add(mockCosign1a);
        mockAllCosignsByUserIdReceivingList.add(mockCosign1b);
        mockAllCosignsByUserIdReceivingList.add(mockCosign1c);
        mockAllCosignsByUserIdReceivingList.add(mockCosign2a);
        mockAllCosignsByUserIdReceivingList.add(mockCosign2b);
        mockAllCosignsByUserIdReceivingList.add(mockCosign2c);
        mockAllCosignsByUserIdReceivingList.add(mockCosign3a);
        mockAllCosignsByUserIdReceivingList.add(mockCosign3b);
        mockAllCosignsByUserIdReceivingList.add(mockCosign3c);

        UsernameDTO mockUsernameDTO1 = UsernameDTO.builder()
                .userId(testUserIdIssuing1)
                .username(testUsernameIssuing1)
                .build();

        UsernameDTO mockUsernameDTO2 = UsernameDTO.builder()
                .userId(testUserIdIssuing2)
                .username(testUsernameIssuing2)
                .build();

        UsernameDTO mockUsernameDTO3 = UsernameDTO.builder()
                .userId(testUserIdIssuing3)
                .username(testUsernameIssuing3)
                .build();


        List<UsernameDTO> mockUsernameDTOSList1 = new ArrayList<>();
        mockUsernameDTOSList1.add(mockUsernameDTO1);
        mockUsernameDTOSList1.add(mockUsernameDTO2);
        mockUsernameDTOSList1.add(mockUsernameDTO3);

        List<UsernameDTO> mockUsernameDTOSList2 = new ArrayList<>();
        mockUsernameDTOSList2.add(mockUsernameDTO1);
        mockUsernameDTOSList2.add(mockUsernameDTO2);
        mockUsernameDTOSList2.add(mockUsernameDTO3);

        List<UsernameDTO> mockUsernameDTOSList3 = new ArrayList<>();
        mockUsernameDTOSList3.add(mockUsernameDTO1);
        mockUsernameDTOSList3.add(mockUsernameDTO2);
        mockUsernameDTOSList3.add(mockUsernameDTO3);


        CosignsForUserDTO mockCosignsForUserDTO1 = CosignsForUserDTO.builder()
                .phraseId(testPhraseId1)
                .listOfCosigners(mockUsernameDTOSList1)
                .build();

        CosignsForUserDTO mockCosignsForUserDTO2 = CosignsForUserDTO.builder()
                .phraseId(testPhraseId2)
                .listOfCosigners(mockUsernameDTOSList2)
                .build();

        CosignsForUserDTO mockCosignsForUserDTO3 = CosignsForUserDTO.builder()
                .phraseId(testPhraseId3)
                .listOfCosigners(mockUsernameDTOSList3)
                .build();

        // mock returns
        when(cosignRepository.findAllByUserIdReceiving(anyLong())).thenReturn(mockAllCosignsByUserIdReceivingList);
        when(userService.getUsernameDTO(anyLong())).thenReturn(mockUsernameDTO1).thenReturn(mockUsernameDTO2).thenReturn(mockUsernameDTO3);

        // expected results
        List<CosignsForUserDTO> expectedCosignsForUserDTOSList = new ArrayList<>();
        expectedCosignsForUserDTOSList.add(mockCosignsForUserDTO1);
        expectedCosignsForUserDTOSList.add(mockCosignsForUserDTO2);
        expectedCosignsForUserDTOSList.add(mockCosignsForUserDTO3);

        // test
        List<CosignsForUserDTO> testCosignsForUserDTOs = cosignService.getAllCosignsForUser(testUserIdReceiving);

        for(int i=0; i<testCosignsForUserDTOs.size(); i++) {
            assertEquals(testCosignsForUserDTOs.get(i).phraseId,expectedCosignsForUserDTOSList.get(i).phraseId);
            System.out.println(
                    "Test phrase " + i + ": expected phrase = "
                            + expectedCosignsForUserDTOSList.get(i).phraseId
                            + " actual phrase = "
                            + testCosignsForUserDTOs.get(i).phraseId
            );
            for(int j=0; j<testCosignsForUserDTOs.get(i).listOfCosigners.size(); j++){
                System.out.println(
                        "Test phrase " + i + " test user " + j
                                + ": expected user id = "
                                + expectedCosignsForUserDTOSList.get(j).listOfCosigners.get(j).userId
                                + " actual user id = "
                                + testCosignsForUserDTOs.get(i).listOfCosigners.get(j).userId
                                + ": expected user name = "
                                + expectedCosignsForUserDTOSList.get(j).listOfCosigners.get(j).username
                                + " actual user name = "
                                + testCosignsForUserDTOs.get(i).listOfCosigners.get(j).username
                );
                assertEquals(testCosignsForUserDTOs.get(i).listOfCosigners.get(j).userId, expectedCosignsForUserDTOSList.get(j).listOfCosigners.get(j).userId);
                assertEquals(testCosignsForUserDTOs.get(i).listOfCosigners.get(j).username,expectedCosignsForUserDTOSList.get(j).listOfCosigners.get(j).username);
            }
        }
    }
}
