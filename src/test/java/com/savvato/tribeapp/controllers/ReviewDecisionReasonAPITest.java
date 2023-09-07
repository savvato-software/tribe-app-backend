package com.savvato.tribeapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.services.*;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewDecisionReasonAPIController.class)
public class ReviewDecisionReasonAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceTRIBEAPP userDetailsServiceTRIBEAPP;

    @MockBean
    private UserPrincipalService userPrincipalService;

    @MockBean
    private ReviewDecisionReasonService reviewDecisionreasonService;

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Before("")
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testReviewDecisionReasonHappyPath() throws Exception {
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);
        rolesSet.add(UserRole.ROLE_PHRASEREVIEWER);

        User user = new User();
        user.setId(1L);
        user.setName(Constants.FAKE_USER_NAME1);
        user.setPassword("phrase_reviewer"); // pw => admin
        user.setEnabled(1);
        user.setRoles(rolesSet);
        user.setCreated();
        user.setLastUpdated();
        user.setEmail(Constants.FAKE_USER_EMAIL1);

        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString())).thenReturn(
                new UserPrincipal(user)
        );
        String auth = AuthServiceImpl.generateAccessToken(user);

        List<ReviewDecisionReasonDTO> rdrDTOList = new ArrayList<>();

        for(int i=0; i<5; i++) {
            ReviewDecisionReasonDTO rdrDTO = ReviewDecisionReasonDTO.builder().build();
            rdrDTO.id = (Integer.toUnsignedLong(i+1));
            rdrDTO.reason = ("testing" + i);
            rdrDTOList.add(rdrDTO);
        }

        Optional<List<ReviewDecisionReasonDTO>> expectedResult = Optional.of(rdrDTOList);
        Mockito.when(reviewDecisionreasonService.getReviewDecisionReasons()).thenReturn(expectedResult);


        this.mockMvc.
                perform(
                        get("/api/review-decision-reason")
                                .header("Authorization", "Bearer " + auth))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testReviewDecisionReasonSadPath() throws Exception {
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);
        rolesSet.add(UserRole.ROLE_PHRASEREVIEWER);

        User user = new User();
        user.setId(1L);
        user.setName(Constants.FAKE_USER_NAME1);
        user.setPassword("phrase_reviewer"); // pw => admin
        user.setEnabled(1);
        user.setRoles(rolesSet);
        user.setCreated();
        user.setLastUpdated();
        user.setEmail(Constants.FAKE_USER_EMAIL1);

        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString())).thenReturn(
                new UserPrincipal(user)
        );
        String auth = AuthServiceImpl.generateAccessToken(user);

        Mockito.when(reviewDecisionreasonService.getReviewDecisionReasons()).thenReturn(Optional.empty());

        this.mockMvc.
                perform(
                        get("/api/review-decision-reason")
                                .header("Authorization", "Bearer " + auth))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}
