package com.savvato.tribeapp.services;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.repositories.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserPrincipalServiceImplTest extends AbstractServiceImplTest {
    @TestConfiguration
    static class UserPrincipalServiceTestContextConfiguration {
        @Bean
        public UserPrincipalService userPrincipalService() {
            return new UserPrincipalServiceImpl();
        }
    }

    @Autowired
    UserPrincipalService userPrincipalService;
    @MockBean
    UserRepository userRepository;

    @Test
    public void getUserPrincipalByNameWhenUserExists() {
        String name = USER1_NAME;
        User user = getUser1();
        UserPrincipal expected = new UserPrincipal(user);
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        UserPrincipal actual = userPrincipalService.getUserPrincipalByName(name);
        verify(userRepository, times(1)).findByName(nameCaptor.capture());
        assertEquals(name, nameCaptor.getValue());
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    public void getUserPrincipalByNameWhenUserDoesntExist() {
        String name = USER1_NAME;
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        UserPrincipal actual = userPrincipalService.getUserPrincipalByName(name);
        verify(userRepository, times(1)).findByName(nameCaptor.capture());
        assertEquals(name, nameCaptor.getValue());
        assertNull(actual);
    }

    @Test
    public void getUserPrincipalByEmailWhenUserExists() {
        String email = USER1_EMAIL;
        User user = getUser1();
        UserPrincipal expected = new UserPrincipal(user);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        UserPrincipal actual = userPrincipalService.getUserPrincipalByEmail(email);
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
        verify(userRepository, times(1)).findByEmail(emailCaptor.capture());
        assertEquals(email, emailCaptor.getValue());
    }

    @Test
    public void getUserPrincipalByEmailWhenUserDoesntExist() {
        String email = USER1_EMAIL;
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        UserPrincipal actual = userPrincipalService.getUserPrincipalByEmail(email);
        verify(userRepository, times(1)).findByEmail(emailCaptor.capture());
        assertEquals(email, emailCaptor.getValue());
        assertNull(actual);
    }
}
