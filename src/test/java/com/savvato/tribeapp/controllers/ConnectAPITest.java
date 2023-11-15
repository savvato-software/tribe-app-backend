package com.savvato.tribeapp.controllers;

import com.google.gson.Gson;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.ConnectRequest;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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


}
