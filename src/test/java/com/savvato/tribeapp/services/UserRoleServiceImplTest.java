package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserRoleServiceImplTest {
    @TestConfiguration
    static class UserRoleServiceTestContextConfiguration {
        @Bean
        public UserRoleService userRoleService() {
            return new UserRoleServiceImpl();
        }
    }

    @Autowired
    UserRoleService userRoleService;

    @MockBean
    UserRoleRepository userRoleRepository;

    @Test
    public void getRoles() {
        Iterable<UserRole> roles = new ArrayList<>(List.of(UserRole.ROLE_PHRASEREVIEWER, UserRole.ROLE_ADMIN, UserRole.ROLE_ACCOUNTHOLDER));
        when(userRoleRepository.findAll()).thenReturn(roles);
        assertThat(userRoleService.getRoles()).usingRecursiveComparison().isEqualTo(roles);
    }

}
