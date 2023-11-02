package com.savvato.tribeapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.AuthRequest;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.services.AuthService;
import com.savvato.tribeapp.services.UserDetailsServiceTRIBEAPP;
import com.savvato.tribeapp.services.UserPrincipalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthAPIController.class)

public class AuthAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;


    @Autowired
    private Gson gson;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserPrincipalService userPrincipalService;

    @MockBean
    private UserDetailsServiceTRIBEAPP userDetailsServiceTRIBEAPP;

    @Test
    public void loginHappyPath() throws Exception {
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);
        rolesSet.add(UserRole.ROLE_PHRASEREVIEWER);
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setName(Constants.FAKE_USER_NAME1);
        expectedUser.setPassword("phrase_reviewer"); // pw => admin
        expectedUser.setEnabled(1);
        expectedUser.setRoles(rolesSet);
        expectedUser.setCreated();
        expectedUser.setLastUpdated();
        expectedUser.setEmail(Constants.FAKE_USER_EMAIL1);
        AuthRequest authRequest = new AuthRequest();
        authRequest.email = expectedUser.getEmail();
        authRequest.password = expectedUser.getPassword();

        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        authentication.setAuthenticated(true);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(userPrincipal.getUser()).thenReturn(expectedUser);
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(expectedUser));
        MvcResult result = this.mockMvc
                .perform(
                        post("/api/public/login")
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(authRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        User actualUser = mapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);


    }

    @Test
    public void loginWhenBadCredentialsErrorThrown() throws Exception {
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);
        rolesSet.add(UserRole.ROLE_PHRASEREVIEWER);
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setName(Constants.FAKE_USER_NAME1);
        expectedUser.setPassword("phrase_reviewer"); // pw => admin
        expectedUser.setEnabled(1);
        expectedUser.setRoles(rolesSet);
        expectedUser.setCreated();
        expectedUser.setLastUpdated();
        expectedUser.setEmail(Constants.FAKE_USER_EMAIL1);
        AuthRequest authRequest = new AuthRequest();
        authRequest.email = expectedUser.getEmail();
        authRequest.password = expectedUser.getPassword();
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Login credentials invalid"));
        this.mockMvc
                .perform(
                        post("/api/public/login")
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist()) // ensure body is empty
                .andReturn();
    }
}
