package com.savvato.tribeapp.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.ReviewDecisionRequest;
import com.savvato.tribeapp.dto.ProfileDTO;
import com.savvato.tribeapp.entities.ReviewDecision;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.services.*;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewDecisionAPIController.class)
public class ReviewDecisionAPITest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceTRIBEAPP userDetailsServiceTRIBEAPP;

    @MockBean
    private UserPrincipalService userPrincipalService;

    @MockBean
    private ReviewDecisionService reviewDecisionService;

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
    public void testReviewDecisionHappyPath() throws Exception {
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

        ReviewDecisionRequest reviewDecisionRequest = new ReviewDecisionRequest();
        reviewDecisionRequest.reviewId = 1L;
        reviewDecisionRequest.reviewerId = 1L;
        reviewDecisionRequest.reasonId = 1L;

        ReviewDecision expectedResult = new ReviewDecision(reviewDecisionRequest.reviewId, reviewDecisionRequest.reviewerId, reviewDecisionRequest.reasonId);
        Mockito.when(reviewDecisionService.saveReviewDecision(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(expectedResult);


        this.mockMvc.
                perform(
                        post("/api/reviewer-decision")
                                .header("Authorization", "Bearer " + auth)
                                .content(objectMapper.writeValueAsString(reviewDecisionRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
