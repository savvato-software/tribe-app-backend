package com.savvato.tribeapp.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.ConnectRequest;
import com.savvato.tribeapp.controllers.dto.ConnectionRemovalRequest;
import com.savvato.tribeapp.controllers.dto.CosignRequest;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import com.savvato.tribeapp.dto.CosignDTO;
import com.savvato.tribeapp.dto.CosignsForUserDTO;
import com.savvato.tribeapp.dto.UsernameDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.CosignRepository;
import com.savvato.tribeapp.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConnectAPIController.class)
public class ConnectAPITest {
    private User user;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Gson gson;

    @MockBean
    private UserDetailsServiceTRIBEAPP userDetailsServiceTRIBEAPP;

    @MockBean
    private UserPrincipalService userPrincipalService;

    @MockBean
    private ConnectService connectService;

    @MockBean CosignService cosignService;

    @MockBean
    private CosignRepository repository;

    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(springSecurity())
                        .build();

        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);
        rolesSet.add(UserRole.ROLE_PHRASEREVIEWER);

        user = new User();
        user.setId(1L);
        user.setName(Constants.FAKE_USER_NAME1);
        user.setPassword("phrase_reviewer"); // pw => admin
        user.setEnabled(1);
        user.setRoles(rolesSet);
        user.setCreated();
        user.setLastUpdated();
        user.setEmail(Constants.FAKE_USER_EMAIL1);
    }

    @Test
    public void getQrCodeStringHappyPath() throws Exception {
        Long userId = 1L;
        String qrCode = "ABCDEFGHIJKL";

        when(connectService.storeQRCodeString(anyLong())).thenReturn(Optional.of(qrCode));
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        this.mockMvc
                .perform(
                        get("/api/connect/{userId}", userId)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(qrCode));

        verify(connectService, times(1)).storeQRCodeString(userIdCaptor.capture());
        assertEquals(userIdCaptor.getValue(), userId);
    }

    @Test
    public void getQrCodeStringWhenQrCodeNotGenerated() throws Exception {

        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long userId = 1L;
        when(connectService.storeQRCodeString(anyLong())).thenReturn(Optional.empty());

        this.mockMvc
                .perform(
                        get("/api/connect/{userId}", userId)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());

        verify(connectService, times(1)).storeQRCodeString(userIdCaptor.capture());
        assertEquals(userIdCaptor.getValue(), userId);
    }

    @Test
    public void connectHappyPath() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ConnectRequest connectRequest = new ConnectRequest();

        connectRequest.requestingUserId = 1L;
        connectRequest.toBeConnectedWithUserId = 2L;
        connectRequest.qrcodePhrase = "ABCDEFGHIJKL";

        when(connectService.validateQRCode(anyString(), anyLong())).thenReturn(true);
        when(connectService.saveConnectionDetails(anyLong(), anyLong())).thenReturn(true);
        ArgumentCaptor<Long> requestingUserIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> toBeConnectedWithUserIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> qrCodePhraseCaptor = ArgumentCaptor.forClass(String.class);
        this.mockMvc
                .perform(
                        post("/api/connect")
                                .content(gson.toJson(connectRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
        verify(connectService, times(1)).validateQRCode(qrCodePhraseCaptor.capture(), requestingUserIdCaptor.capture());
        verify(connectService, times(1)).saveConnectionDetails(requestingUserIdCaptor.capture(), toBeConnectedWithUserIdCaptor.capture());
        assertEquals(qrCodePhraseCaptor.getValue(), connectRequest.qrcodePhrase);
        assertEquals(requestingUserIdCaptor.getValue(), connectRequest.requestingUserId);
        assertEquals(toBeConnectedWithUserIdCaptor.getValue(), connectRequest.toBeConnectedWithUserId);
    }

    @Test
    public void connectSadPath() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ConnectRequest connectRequest = new ConnectRequest();

        connectRequest.requestingUserId = 1L;
        connectRequest.toBeConnectedWithUserId = 2L;
        connectRequest.qrcodePhrase = "ABCDEFGHIJKL";

        when(connectService.validateQRCode(anyString(), anyLong())).thenReturn(true);
        when(connectService.saveConnectionDetails(anyLong(), anyLong())).thenReturn(false);
        this.mockMvc
                .perform(
                        post("/api/connect")
                                .content(gson.toJson(connectRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();

    }



    @Test
    public void connectWhenQrCodeInvalid() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ConnectRequest connectRequest = new ConnectRequest();

        connectRequest.requestingUserId = 1L;
        connectRequest.toBeConnectedWithUserId = 2L;
        connectRequest.qrcodePhrase = "invalid code";

        when(connectService.validateQRCode(anyString(), anyLong())).thenReturn(false);
        ArgumentCaptor<Long> toBeConnectedWithUserIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> qrCodePhraseCaptor = ArgumentCaptor.forClass(String.class);
        this.mockMvc
                .perform(
                        post("/api/connect")
                                .content(gson.toJson(connectRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();
        verify(connectService, times(1)).validateQRCode(qrCodePhraseCaptor.capture(), toBeConnectedWithUserIdCaptor.capture());
        verify(connectService, never()).saveConnectionDetails(any(), any());
        assertEquals(qrCodePhraseCaptor.getValue(), connectRequest.qrcodePhrase);
        assertEquals(toBeConnectedWithUserIdCaptor.getValue(), connectRequest.toBeConnectedWithUserId);
    }

    @Test
    public void saveCosign() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);

        Long testUserIdIssuing = 1L;
        Long testUserIdReceiving = 2L;
        Long testPhraseId = 1L;

        CosignDTO mockCosignDTO = CosignDTO.builder().build();
        mockCosignDTO.userIdIssuing = testUserIdIssuing;
        mockCosignDTO.userIdReceiving = testUserIdReceiving;
        mockCosignDTO.phraseId = testPhraseId;

        CosignRequest cosignRequest = new CosignRequest();
        cosignRequest.userIdIssuing = testUserIdIssuing;
        cosignRequest.userIdReceiving = testUserIdReceiving;
        cosignRequest.phraseId = testPhraseId;

        when(cosignService.saveCosign(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(mockCosignDTO));

        this.mockMvc
                .perform(
                        post("/api/connect/cosign")
                                .content(gson.toJson(cosignRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"userIdIssuing\":1,\"userIdReceiving\":2,\"phraseId\":1}"));

    }

    @Test
    public void saveCosignSadPathUserCosignsThemselves() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);

        Long testUserIdIssuing = 1L;
        Long testUserIdReceiving = 1L;
        Long testPhraseId = 1L;

        CosignRequest cosignRequest = new CosignRequest();
        cosignRequest.userIdIssuing = testUserIdIssuing;
        cosignRequest.userIdReceiving = testUserIdReceiving;
        cosignRequest.phraseId = testPhraseId;

        when(cosignService.saveCosign(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());

        this.mockMvc
                .perform(
                        post("/api/connect/cosign")
                                .content(gson.toJson(cosignRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"responseMessage\":\"Users may not cosign themselves.\"}"));
    }

    public void removeConnectionHappyPath() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ConnectionRemovalRequest connectionDeleteRequest = new ConnectionRemovalRequest();
        connectionDeleteRequest.requestingUserId = 1L;
        connectionDeleteRequest.connectedWithUserId = 2L;
        when(connectService.removeConnection(any())).thenReturn(true);
        ArgumentCaptor<ConnectionRemovalRequest> connectionDeleteRequestCaptor = ArgumentCaptor.forClass(ConnectionRemovalRequest.class);
        this.mockMvc
                .perform(
                        delete("/api/connect")
                                .content(gson.toJson(connectionDeleteRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
        verify(connectService, times(1)).removeConnection(connectionDeleteRequestCaptor.capture());
        assertThat(connectionDeleteRequestCaptor.getValue()).usingRecursiveComparison().isEqualTo(connectionDeleteRequest);

    }

//    @Test
    public void removeConnectionWhenRemovalUnsuccessful() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);

        ConnectionRemovalRequest connectionDeleteRequest = new ConnectionRemovalRequest();
        connectionDeleteRequest.requestingUserId = 1L;
        connectionDeleteRequest.connectedWithUserId = 2L;
        when(connectService.removeConnection(any())).thenReturn(false);
        ArgumentCaptor<ConnectionRemovalRequest> connectionDeleteRequestCaptor = ArgumentCaptor.forClass(ConnectionRemovalRequest.class);
        this.mockMvc
                .perform(
                        delete("/api/connect")
                                .content(gson.toJson(connectionDeleteRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("false"))
                .andReturn();
        verify(connectService, times(1)).removeConnection(connectionDeleteRequestCaptor.capture());
        assertThat(connectionDeleteRequestCaptor.getValue()).usingRecursiveComparison().isEqualTo(connectionDeleteRequest);

    }

    @Test
    public void testGetConnectionsHappyPath() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long toBeConnectedWithUserId = 1L;
        Long requestingUserId = 2L;

        ConnectOutgoingMessageDTO returnDTO = ConnectOutgoingMessageDTO
                .builder()
                .connectionError(null)
                .connectionSuccess(true)
                .message("")
                .to(requestingUserId)
                .build();

        List<ConnectOutgoingMessageDTO> expectedReturnDtoList = new ArrayList<>();
        expectedReturnDtoList.add(returnDTO);

        when(connectService.getAllConnectionsForAUser(anyLong())).thenReturn(expectedReturnDtoList);

        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/connect/{userId}/all", toBeConnectedWithUserId)
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        Type connectOutgoingMessageListDTOType = new TypeToken<List<ConnectOutgoingMessageDTO>>(){}.getType();

        List<ConnectOutgoingMessageDTO> actualConnectOutingMessages =
                gson.fromJson(result.getResponse().getContentAsString(), connectOutgoingMessageListDTOType);

        assertThat(actualConnectOutingMessages).usingRecursiveComparison().isEqualTo(expectedReturnDtoList);
    }

    @Test
    public void testGetConnectionsSadPath() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);

        Long userId = 1L;

        when(connectService.getAllConnectionsForAUser(anyLong())).thenReturn(null);

        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/connect/{userId}/all", userId)
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isBadRequest())
                        .andReturn();

    }

    @Test
    public void testGetCosignersForUserAttribute() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);

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

        List<UsernameDTO> mockUsernameDTOList = new ArrayList<>();
        mockUsernameDTOList.add(mockUsernameDTO);

        // mock returns
        when(cosignService.getCosignersForUserAttribute(anyLong(),anyLong())).thenReturn(mockUsernameDTOList);

        // test
        this.mockMvc
                .perform(
                        get("/api/connect/cosign/{userIdReceiving}/{phraseId}",testUserIdReceiving,testPhraseId)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"userId\":1,\"username\":\"test\"}]"));
    }

    @Test
    public void testGetAllCosignsForUser() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);

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


        List<CosignsForUserDTO> mockCosignsForUserDTOList = new ArrayList<>();
        mockCosignsForUserDTOList.add(mockCosignsForUserDTO1);
        mockCosignsForUserDTOList.add(mockCosignsForUserDTO2);
        mockCosignsForUserDTOList.add(mockCosignsForUserDTO3);

        // mock returns
        when(cosignService.getAllCosignsForUser(anyLong())).thenReturn(mockCosignsForUserDTOList);

        // test
        this.mockMvc
                .perform(
                        get("/api/connect/cosign/{userIdReceiving}/all",testUserIdReceiving)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[" +
                                "{\"phraseId\":1,\"listOfCosigners\":" +
                                    "[" +
                                        "{\"userId\":1,\"username\":\"test1\"}," +
                                        "{\"userId\":2,\"username\":\"test2\"}," +
                                        "{\"userId\":3,\"username\":\"test3\"}" +
                                    "]" +
                                "}," +
                                "{\"phraseId\":2,\"listOfCosigners\":" +
                                    "[" +
                                        "{\"userId\":1,\"username\":\"test1\"}," +
                                        "{\"userId\":2,\"username\":\"test2\"}," +
                                        "{\"userId\":3,\"username\":\"test3\"}" +
                                    "]" +
                                "}," +
                                "{\"phraseId\":3,\"listOfCosigners\":" +
                                    "[" +
                                        "{\"userId\":1,\"username\":\"test1\"}," +
                                        "{\"userId\":2,\"username\":\"test2\"}," +
                                        "{\"userId\":3,\"username\":\"test3\"}" +
                                    "]" +
                                "}]"));
    }
}
