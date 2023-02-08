package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.entities.UserRoleMap;
import com.savvato.tribeapp.repositories.UserRepository;
import com.savvato.tribeapp.repositories.UserRoleMapRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserRoleMapServiceImplTest extends AbstractServiceImplTest {

    @TestConfiguration
    static class UserRoleMapServiceTestContextConfiguration {
        @Bean
        public UserRoleMapService userRoleMapService() {
            return new UserRoleMapServiceImpl();
        }
    }
    @Autowired
    private UserRoleMapService userRoleMapService;
    @MockBean
    private UserRole userRole;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserRoleMapRepository userRoleMapRepository;
    @Test
    public void testAddRolesToUser() {
        // create a new array list with ADMIN, ACCOUNT_HOLDER, PHRASE_REVIEWER in it
        ArrayList<String> expectedRoles = new ArrayList<String>(List.of("ADMIN", "ACCOUNTHOLDER", "PHRASE_REVIEWER"));

        User user = getUser1();
        user.setRoles(getUserRoles_AccountHolder());

        UserRoleMap admin = new UserRoleMap(user.getId(),1L);
        UserRoleMap accountholder = new UserRoleMap(user.getId(),2L);
        UserRoleMap phrasereviewer = new UserRoleMap(user.getId(),3L);

        // return true if userRoleMapRepository.save() is called at all
        Mockito.when(userRoleMapRepository.save(Mockito.any()))
                .thenReturn(admin)
                .thenReturn(accountholder)
                .thenReturn(phrasereviewer);

        // call userRoleMapService.addRolesToUser() with parameters userRequest.userId and the array list
        userRoleMapService.addRolesToUser(user.getId(), expectedRoles);

        ArgumentCaptor<UserRoleMap> arg1 = ArgumentCaptor.forClass(UserRoleMap.class);
        verify(userRoleMapRepository, times(3)).save(arg1.capture());
        assertEquals(arg1.getAllValues().get(0).getUserId(), user.getId());
        assertEquals(arg1.getAllValues().get(0).getUserRoleId(),userRole.ROLE_ADMIN.getId());

        assertEquals(arg1.getAllValues().get(1).getUserId(), user.getId());
        assertEquals(arg1.getAllValues().get(2).getUserId(), user.getId());
    }

    @Test
    public void testAddIncorrectRolesToUser() {
        // create a new array list with FAKE_ROLE, ADMIN
        ArrayList<String> expectedRoles = new ArrayList<String>(List.of("FAKE_ROLE", "ADMIN"));

        User user = getUser1();
        user.setRoles(getUserRoles_AccountHolder());

        // call userRoleMapService.addRolesToUser() with parameters userRequest.userId and the array list
        boolean returnedFalse = userRoleMapService.addRolesToUser(user.getId(), expectedRoles);

        assertThat(returnedFalse).isFalse();

        verify(userRoleMapRepository, never()).save(Mockito.any());
    }

}
