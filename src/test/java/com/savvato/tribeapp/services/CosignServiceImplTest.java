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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @Test
    public void saveCosignFailsWhenIdsEqual() {
        Long testUserIdIssuing = 1L;
        Long testUserIdReceiving = 1L;
        Long testPhraseId = 1L;

        Optional<CosignDTO> cosignDTO = cosignService.saveCosign(testUserIdIssuing, testUserIdReceiving, testPhraseId);

        verify(cosignRepository, never()).save(Mockito.any());
        assertThat(cosignDTO).usingRecursiveComparison().isEqualTo(Optional.empty());
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
    public void testGetAllCosignsForUserWithThreeCosignsForThreePhrases() {
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
        List<Cosign> mockAllCosignsByUserIdReceivingList = listBuilderCosignForUser(3,3); // See helper method listBuilderCosignForUser below

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
            for(int j=0; j<testCosignsForUserDTOs.get(i).listOfCosigners.size(); j++){
                assertEquals(testCosignsForUserDTOs.get(i).listOfCosigners.get(j).userId, expectedCosignsForUserDTOSList.get(j).listOfCosigners.get(j).userId);
                assertEquals(testCosignsForUserDTOs.get(i).listOfCosigners.get(j).username,expectedCosignsForUserDTOSList.get(j).listOfCosigners.get(j).username);
            }
        }
    }

    // Helper method to build a list of cosigns for a user with the specified amount of phrases and the specified amount of cosigners per phrase.
    private List<Cosign> listBuilderCosignForUser (int phraseCount, int userPerPhraseCount) {
        List<Cosign> list = new ArrayList<>();
        Long userIdReceiving = userPerPhraseCount + 1L;

        for(int i=1; i<=phraseCount; i++) {
            Cosign cosign = new Cosign();
            cosign.setPhraseId((long)i);
            for(int j=1; j<=userPerPhraseCount; j++)
                cosign.setUserIdIssuing((long)j);
            cosign.setUserIdReceiving(userIdReceiving);
            list.add(cosign);
        }
        return list;
    }
}
