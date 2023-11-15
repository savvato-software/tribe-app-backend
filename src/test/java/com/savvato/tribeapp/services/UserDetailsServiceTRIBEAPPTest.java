package com.savvato.tribeapp.services;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserDetailsServiceTRIBEAPPTest extends AbstractServiceImplTest {
    @TestConfiguration
    static class UserDetailsServiceTRIBEAPPTestContextConfiguration {
        @Bean
        public UserDetailsService UserDetailsService() {
            return new UserDetailsServiceTRIBEAPP();
        }
    }

    @Autowired
    UserDetailsService userDetailsService;
    @MockBean
    UserRepository userRepository;

    @Test
    public void loadUserByUsernameWhenUserExists() {
        User user = getUser1();
        String email = user.getEmail();
        Optional<User> userOpt = Optional.of(user);
        UserDetails expectedRtn = new UserPrincipal(user);
        when(userRepository.findByEmail(anyString())).thenReturn(userOpt);
        UserDetails actualRtn = userDetailsService.loadUserByUsername(email);
        assertThat(actualRtn).usingRecursiveComparison().isEqualTo(expectedRtn);
    }

    @Test
    public void loadUserByUsernameWhenUserNotFound() {
        String email = USER1_EMAIL;
        Optional<User> userOpt = Optional.empty();
        when(userRepository.findByEmail(anyString())).thenReturn(userOpt);
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        }, email);
    }
}
