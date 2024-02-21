package com.savvato.tribeapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.NotificationRequest;
import com.savvato.tribeapp.dto.GenericMessageDTO;
import com.savvato.tribeapp.dto.NotificationDTO;
import com.savvato.tribeapp.entities.NotificationType;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationAPIController.class)
public class NotificationAPITest {
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
    private NotificationService notificationService;

    @MockBean
    private GenericMessageService genericMessageService;

    @Captor
    private ArgumentCaptor<Long> notificationIdCaptor;

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
    public void updateNotificationWhenNotificationIsAlreadyRead() throws Exception {

        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.id = 1L;
        when(notificationService.checkNotificationReadStatus(1L)).thenReturn(true);
        when(genericMessageService.createDTO(anyString()))
                .thenReturn(GenericMessageDTO.builder().responseMessage("Notification is already read").build());

        MvcResult result = this.mockMvc
                .perform(
                        put("/api/notifications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(notificationRequest))
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("responseMessage").value(("Notification is already read")))
                .andReturn(); // Get the MvcResult


        verify(notificationService, times(1))
                .checkNotificationReadStatus(notificationIdCaptor.capture());
        assertEquals(notificationIdCaptor.getValue(), notificationRequest.id);
    }

    @Test
    public void updateNotificationWhenNotificationIsNotRead() throws Exception {

        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.id = 1L;
        when(notificationService.checkNotificationReadStatus(1L)).thenReturn(false);
        when(genericMessageService.createDTO(anyString()))
                .thenReturn(GenericMessageDTO.builder().responseMessage("Notification read status updated").build());

        MvcResult result = this.mockMvc
                .perform(
                        put("/api/notifications")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(notificationRequest))
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("responseMessage").value("Notification read status updated"))
                .andReturn(); // Get the MvcResult

        verify(notificationService, times(1))
                .checkNotificationReadStatus(notificationIdCaptor.capture());
        assertEquals(notificationIdCaptor.getValue(), notificationRequest.id);

        verify(notificationService, times(1))
                .updateNotificationReadStatus(notificationIdCaptor.capture());
        assertEquals(notificationIdCaptor.getValue(), notificationRequest.id);
    }

    @Test
    public void deleteNotificationWhenNotificationExists() throws Exception {

        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.id = 1L;
        when(notificationService.checkNotificationExists(1L)).thenReturn(true); // any other value will return false
        when(genericMessageService.createDTO(anyString()))
                .thenReturn(GenericMessageDTO.builder().responseMessage("Notification deleted").build());

        MvcResult result = this.mockMvc
                .perform(delete("/api/notifications/{id}", notificationRequest.id)
                        .header("Authorization", "Bearer " + auth)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("responseMessage").value("Notification deleted")).andReturn(); // Get the MvcResult
        verify(notificationService, times(1)).deleteNotification(notificationIdCaptor.capture());
        assertEquals(notificationIdCaptor.getValue(), notificationRequest.id);
    }

    @Test
    public void deleteNotificationWhenNotificationNonExistent() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.id = 1L;
        when(notificationService.checkNotificationExists(anyLong())).thenReturn(false);

        MvcResult result = this.mockMvc
                .perform(delete("/api/notifications/{id}", notificationRequest.id)
                        .header("Authorization", "Bearer " + auth)
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andReturn(); // Get the MvcResult

        verify(notificationService, times(1)).checkNotificationExists(notificationIdCaptor.capture());
        assertEquals(notificationIdCaptor.getValue(), notificationRequest.id);

        verify(notificationService, never()).deleteNotification(any());
    }

    @Test
    public void getUserNotifications() throws Exception {
        when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        Long userId = 1L;
        NotificationDTO notificationDTO =
                NotificationDTO.builder()
                        .id(userId)
                        .description(NotificationType.ATTRIBUTE_REQUEST_REJECTED.getName())
                        .body("Your notification was rejected due to inappropriate content")
                        .iconUrl(NotificationType.ATTRIBUTE_REQUEST_REJECTED.getIconUrl())
                        .build();

        List<NotificationDTO> expectedNotificationDTOList = List.of(notificationDTO);

        when(notificationService.getUserNotifications(anyLong()))
                .thenReturn(expectedNotificationDTOList);
        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/notifications/user/{user_id}", userId)
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        verify(notificationService, times(1)).getUserNotifications(notificationIdCaptor.capture());
        assertEquals(notificationIdCaptor.getValue(), userId);
        Type notificationDTOListType = new TypeToken<List<NotificationDTO>>() {
        }.getType();
        List<NotificationDTO> actualNotificationDTOList =
                gson.fromJson(result.getResponse().getContentAsString(), notificationDTOListType);
        assertThat(actualNotificationDTOList)
                .usingRecursiveComparison()
                .isEqualTo(expectedNotificationDTOList);
    }
}
