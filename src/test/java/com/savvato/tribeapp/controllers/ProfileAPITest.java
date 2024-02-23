package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.dto.ProfileDTO;
import com.savvato.tribeapp.dto.GenericResponseDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.services.*;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileAPIController.class)
public class ProfileAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceTRIBEAPP userDetailsServiceTRIBEAPP;

    @MockBean
    private GenericResponseService GenericResponseService;

    @MockBean
    private UserPrincipalService userPrincipalService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before("")
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testProfileHappyPath() throws Exception {

        // TODO: Move these constants to their own classes, and reference them here.
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);

        User user = new User();
        user.setId(1L);
        user.setName(Constants.FAKE_USER_NAME1);
        user.setPassword("admin"); // pw => admin
        user.setEnabled(1);
        user.setRoles(rolesSet);
        user.setCreated();
        user.setLastUpdated();
        user.setEmail(Constants.FAKE_USER_EMAIL1);

        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString())).thenReturn(
                new UserPrincipal(user)
        );

        Mockito.when(profileService.getByUserId(Mockito.anyLong())).thenReturn(
                Optional.of(ProfileDTO.builder()
                        .name(Constants.FAKE_USER_NAME1)
                        .email(Constants.FAKE_USER_EMAIL1)
                        .phone(Constants.FAKE_USER_PHONE1)
                        .build())
        );

        String auth = AuthServiceImpl.generateAccessToken(user);

        this.mockMvc.
                perform(
                    get("/api/profile/1")
                    .header("Authorization", "Bearer " + auth)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(Constants.FAKE_USER_NAME1));

              
                
    }
     @Test
    public void testProfileHappyPathUpdate() throws Exception {
        
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);
        
        User user = new User();
        user.setId(1L);
        user.setName(Constants.FAKE_USER_NAME1);
        user.setPassword("admin"); // pw => admin
        user.setEnabled(1);
        user.setRoles(rolesSet);
        user.setCreated();
        user.setLastUpdated();
        user.setEmail(Constants.FAKE_USER_EMAIL1);
        
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString())).thenReturn(
                new UserPrincipal(user)
        );
        

        Mockito.when(profileService.update(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(true);

         when(GenericResponseService.createDTO(
                 anyBoolean()))
                 .thenReturn(GenericResponseDTO.builder()
                         .booleanMessage(true)
                         .build());

        String auth = AuthServiceImpl.generateAccessToken(user);

        this.mockMvc.
        perform(
            put("/api/profile/1")
            .header("Authorization", "Bearer " + auth)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"userId\":\"1\",\"name\":\"bob\",\"email\":\"admin@app.com\",\"phone\":\"3035551212\"}")
        )
        
        .andExpect(status().isOk())
        .andExpect(jsonPath("booleanMessage").value(true));

} 

@Test
public void testProfileUnHappyPathUpdate() throws Exception {
    
    Set<UserRole> rolesSet = new HashSet<>();
    rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
    rolesSet.add(UserRole.ROLE_ADMIN);
    
    User user = new User();
    user.setId(1L);
    user.setName(Constants.FAKE_USER_NAME1);
    user.setPassword("admin"); // pw => admin
    user.setEnabled(1);
    user.setRoles(rolesSet);
    user.setCreated();
    user.setLastUpdated();
    user.setEmail(Constants.FAKE_USER_EMAIL1);
    
    Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString())).thenReturn(
            new UserPrincipal(user)
    );
    

    Mockito.when(profileService.update(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
    .thenReturn(false);

    when(GenericResponseService.createDTO(
            anyBoolean()))
            .thenReturn(GenericResponseDTO.builder()
                    .booleanMessage(false)
                    .build());

    String auth = AuthServiceImpl.generateAccessToken(user);

    this.mockMvc.
    perform(
        put("/api/profile/1")
        .header("Authorization", "Bearer " + auth)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"userId\":\"1\",\"name\":\"bob\",\"email\":\"admin\",\"phone\":\"3035551212\"}")
    )
    
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("booleanMessage").value(false));

} 

}