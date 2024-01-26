package com.savvato.tribeapp.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.AttributesRequest;
import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.dto.PhraseDTO;
import com.savvato.tribeapp.dto.ToBeReviewedDTO;
import com.savvato.tribeapp.entities.NotificationType;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttributesAPIController.class)
public class AttributesAPITest {
    private UserPrincipal userPrincipal;
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
    private AttributesService attributesService;

    @MockBean
    private PhraseService phraseService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserPhraseService userPhraseService;

    @MockBean
    private ReviewSubmittingUserService reviewSubmittingUserService;

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
    public void getAttributesForUserWhenAttributesFound() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long userId = 1L;
        PhraseDTO phraseDTO =
                PhraseDTO.builder()
                        .verb("plays")
                        .noun("chess")
                        .adverb("competitively")
                        .preposition("")
                        .build();
        AttributeDTO attributeDTO = AttributeDTO.builder().phrase(phraseDTO).build();
        List<AttributeDTO> expectedAttributes = List.of(attributeDTO);
        Optional<List<AttributeDTO>> opt = Optional.of(expectedAttributes);
        when(attributesService.getAttributesByUserId(anyLong())).thenReturn(opt);
        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/attributes/{userId}", userId)
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        Type attributeDTOListType = new TypeToken<List<AttributeDTO>>() {
        }.getType();

        List<AttributeDTO> actualAttributes =
                gson.fromJson(result.getResponse().getContentAsString(), attributeDTOListType);
        assertThat(actualAttributes).usingRecursiveComparison().isEqualTo(expectedAttributes);
    }

    @Test
    public void getAttributesForUserWhenNoAttributesFound() throws Exception {
        Long userId = 1L;
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        when(attributesService.getAttributesByUserId(anyLong())).thenReturn(Optional.empty());
        this.mockMvc
                .perform(
                        get("/api/attributes/{userId}", userId)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist()) // ensure body is empty
                .andReturn();
    }

    @Test
    public void applyPhraseToUserWhenPhraseValidAndApplicable() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long userId = 1L;
        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.userId = userId;
        attributesRequest.id = 1L;
        attributesRequest.adverb = "competitively";
        attributesRequest.verb = "plays";
        attributesRequest.noun = "chess";
        attributesRequest.preposition = "";
        String notificationContent = "Your attribute has been approved!";
        when(phraseService.isPhraseValid(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);
        when(phraseService.applyPhraseToUser(
                anyLong(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);
        when(notificationService.createNotification(
                any(NotificationType.class), anyLong(), anyString(), anyString()))
                .thenReturn(null);
        ArgumentCaptor<NotificationType> notificationTypeCaptor =
                ArgumentCaptor.forClass(NotificationType.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> notificationTypeNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> notificationContentCaptor = ArgumentCaptor.forClass(String.class);

        this.mockMvc
                .perform(
                        post("/api/attributes")
                                .content(gson.toJson(attributesRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();

        verify(notificationService, times(1))
                .createNotification(
                        notificationTypeCaptor.capture(),
                        userIdCaptor.capture(),
                        notificationTypeNameCaptor.capture(),
                        notificationContentCaptor.capture());
        assertThat(notificationTypeCaptor.getValue()).usingRecursiveComparison().isEqualTo(NotificationType.ATTRIBUTE_REQUEST_APPROVED);
        assertEquals(userIdCaptor.getValue(), userId);
        assertEquals(
                notificationTypeNameCaptor.getValue(),
                NotificationType.ATTRIBUTE_REQUEST_APPROVED.getName());
        assertEquals(notificationContentCaptor.getValue(), notificationContent);
    }

    @Test
    public void applyPhraseToUserWhenPhraseValidButNotApplied() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long userId = 1L;
        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.userId = userId;
        attributesRequest.id = 1L;
        attributesRequest.adverb = "competitively";
        attributesRequest.verb = "plays";
        attributesRequest.noun = "chess";
        attributesRequest.preposition = "";

        when(phraseService.isPhraseValid(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);
        when(phraseService.applyPhraseToUser(
                anyLong(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(notificationService.createNotification(
                any(NotificationType.class), anyLong(), anyString(), anyString()))
                .thenReturn(null);
        ArgumentCaptor<NotificationType> notificationTypeCaptor =
                ArgumentCaptor.forClass(NotificationType.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> notificationTypeNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> notificationContentCaptor = ArgumentCaptor.forClass(String.class);
        String notificationContent =
                "Your attribute was rejected. This attribute is unsuitable and cannot be applied to users.";
        this.mockMvc
                .perform(
                        post("/api/attributes")
                                .content(gson.toJson(attributesRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();

        verify(notificationService, times(1))
                .createNotification(
                        notificationTypeCaptor.capture(),
                        userIdCaptor.capture(),
                        notificationTypeNameCaptor.capture(),
                        notificationContentCaptor.capture());
        assertThat(notificationTypeCaptor.getValue()).usingRecursiveComparison().isEqualTo(NotificationType.ATTRIBUTE_REQUEST_REJECTED);
        assertEquals(userIdCaptor.getValue(), userId);
        assertEquals(
                notificationTypeNameCaptor.getValue(),
                NotificationType.ATTRIBUTE_REQUEST_REJECTED.getName());
        assertEquals(notificationContentCaptor.getValue(), notificationContent);
    }

    @Test
    public void applyPhraseToUserWhenPhraseInvalid() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long userId = 1L;
        AttributesRequest attributesRequest = new AttributesRequest();
        attributesRequest.userId = userId;
        attributesRequest.id = 1L;
        attributesRequest.adverb = "competitively";
        attributesRequest.verb = "plays";
        attributesRequest.noun = "chess";
        attributesRequest.preposition = "";

        when(phraseService.isPhraseValid(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(notificationService.createNotification(
                any(NotificationType.class), anyLong(), anyString(), anyString()))
                .thenReturn(null);
        ArgumentCaptor<NotificationType> notificationTypeCaptor =
                ArgumentCaptor.forClass(NotificationType.class);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> notificationTypeNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> notificationContentCaptor = ArgumentCaptor.forClass(String.class);
        String notificationContent =
                "Your attribute was rejected. This attribute is unsuitable and cannot be applied to users.";
        this.mockMvc
                .perform(
                        post("/api/attributes")
                                .content(gson.toJson(attributesRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();
        verify(phraseService, never())
                .applyPhraseToUser(anyLong(), anyString(), anyString(), anyString(), anyString());
        verify(notificationService, times(1))
                .createNotification(
                        notificationTypeCaptor.capture(),
                        userIdCaptor.capture(),
                        notificationTypeNameCaptor.capture(),
                        notificationContentCaptor.capture());
        assertThat(notificationTypeCaptor.getValue()).usingRecursiveComparison().isEqualTo(NotificationType.ATTRIBUTE_REQUEST_REJECTED);
        assertEquals(userIdCaptor.getValue(), userId);
        assertEquals(
                notificationTypeNameCaptor.getValue(),
                NotificationType.ATTRIBUTE_REQUEST_REJECTED.getName());
        assertEquals(notificationContentCaptor.getValue(), notificationContent);
    }

    @Test
    public void deletePhraseFromUserHappyPath() throws Exception {
        String phraseId = "1";
        String userId = "1";
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);

        doNothing().when(userPhraseService).deletePhraseFromUser(anyLong(), anyLong());

        this.mockMvc
                .perform(
                        delete("/api/attributes")
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8")
                                .param("phraseId", phraseId)
                                .param("userId", userId))

                .andExpect(status().isOk())
                .andReturn();

        verify(userPhraseService, times(1)).deletePhraseFromUser(Long.parseLong(phraseId), Long.parseLong(userId));
    }

    @Test
    public void getUserPhrasesToBeReviewedHappyPath() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long userId = 1L;
        ToBeReviewedDTO toBeReviewedDTO =
                ToBeReviewedDTO.builder()
                        .id(1L)
                        .hasBeenGroomed(true)
                        .verb("plays")
                        .noun("sports")
                        .adverb("never")
                        .preposition("")
                        .build();
        List<ToBeReviewedDTO> expectedToBeReviewed = List.of(toBeReviewedDTO);

        when(reviewSubmittingUserService.getUserPhrasesToBeReviewed(anyLong())).thenReturn(expectedToBeReviewed);
        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/attributes/in-review/{userId}", userId)
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        Type toBeReviewedDTOListType = new TypeToken<List<ToBeReviewedDTO>>() {
        }.getType();

        List<ToBeReviewedDTO> actualAttributes =
                gson.fromJson(result.getResponse().getContentAsString(), toBeReviewedDTOListType);
        assertThat(actualAttributes).usingRecursiveComparison().isEqualTo(expectedToBeReviewed);
    }

    @Test
    public void getUserPhrasesToBeReviewedHappyPathNoPhrases() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long userId = 1L;

        when(reviewSubmittingUserService.getUserPhrasesToBeReviewed(anyLong())).thenReturn(new ArrayList<>());

        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/attributes/in-review/{userId}", userId)
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        Type toBeReviewedDTOListType = new TypeToken<List<ToBeReviewedDTO>>() {
        }.getType();

        List<ToBeReviewedDTO> actualAttributes =
                gson.fromJson(result.getResponse().getContentAsString(), toBeReviewedDTOListType);
        assertThat(actualAttributes).isEmpty();
    }
}
