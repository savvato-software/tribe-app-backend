package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.dto.ProfileDTO;
import com.savvato.tribeapp.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)

public class ProfileServiceImplTest extends AbstractServiceImplTest {
    @TestConfiguration
    static class ProfileServiceTestContextConfiguration {
        @Bean
        public ProfileService profileService() {
            return new ProfileServiceImpl();
        }
    }

    @Autowired
    ProfileService profileService;
    @MockBean
    private UserService userService;

    @Test
    public void getByUserIdWhenUserExists() {
        User user = getUser1();
        Long userId = 1L;
        ProfileDTO profile = ProfileDTO.builder().name(user.getName()).phone(user.getPhone()).email(user.getEmail()).created(String.valueOf(user.getCreated().getTime())).lastUpdated(String.valueOf(user.getLastUpdated().getTime())).build();
        Optional<User> userOpt = Optional.of(user);
        Optional<ProfileDTO> profileOpt = Optional.of(profile);

        when(userService.findById(any())).thenReturn(userOpt);
        Optional<ProfileDTO> result = profileService.getByUserId(userId);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService, times(1)).findById(userIdCaptor.capture());
        assertEquals(userIdCaptor.getValue(), userId);
        assertThat(result).usingRecursiveComparison().isEqualTo(profileOpt);
    }

    @Test
    public void getByUserIdWhenUserNotFound() {
        Long userId = 100L;
        Optional<User> userOpt = Optional.empty();
        ProfileDTO profile = ProfileDTO.builder().build();
        Optional<ProfileDTO> profileOpt = Optional.of(profile);

        when(userService.findById(any())).thenReturn(userOpt);
        Optional<ProfileDTO> result = profileService.getByUserId(userId);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService, times(1)).findById(userIdCaptor.capture());
        assertEquals(userIdCaptor.getValue(), userId);
        assertThat(result).usingRecursiveComparison().isEqualTo(profileOpt);
    }

    @Test
    public void updateWhenUserUpdateSucceeds() {
        User user = getUser1();
        UserRequest userRequest = new UserRequest();
        Long userId = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        String phone = user.getPhone();
        userRequest.id = userId;
        userRequest.name = name;
        userRequest.email = email;
        userRequest.phone = phone;
        Optional<User> userOpt = Optional.of(user);
        ArgumentCaptor<UserRequest> userRequestArgumentCaptor = ArgumentCaptor.forClass(UserRequest.class);
        when(userService.update(any())).thenReturn(userOpt);
        boolean result = profileService.update(userId, name, email, phone);
        verify(userService, times(1)).update(userRequestArgumentCaptor.capture());
        assertThat(userRequestArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(userRequest);
        assertTrue(result);
    }

    @Test
    public void updateWhenUserUpdateFails() {
        Long userId = USER1_ID;
        String name = USER1_NAME;
        String email = USER1_EMAIL;
        String phone = USER1_PHONE;
        Optional<User> userOpt = Optional.empty();
        ArgumentCaptor<UserRequest> userRequestArgumentCaptor = ArgumentCaptor.forClass(UserRequest.class);

        when(userService.update(any())).thenReturn(userOpt);
        boolean result = profileService.update(userId, name, email, phone);
        verify(userService, times(1)).update(userRequestArgumentCaptor.capture());
        assertEquals(userRequestArgumentCaptor.getValue().id, userId);
        assertEquals(userRequestArgumentCaptor.getValue().name, name);
        assertEquals(userRequestArgumentCaptor.getValue().email, email);
        assertEquals(userRequestArgumentCaptor.getValue().phone, phone);
        assertFalse(result);
    }
}