package com.savvato.tribeapp.services;

import com.savvato.tribeapp.repositories.UserRepository;
import com.savvato.tribeapp.repositories.UserRoleMapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserRoleMapServiceImplTest extends AbstractServiceImplTest {
    @Autowired
    private UserRoleMapService userRoleMapService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserRoleMapRepository userRoleMapRepository;
    @Test
    public void testAddRolesToUser() {
        // create a new userRequest, user ID: 3,
        // create a new user using userService.createNewUser
        // create a new array list with ADMIN, ACCOUNT_HOLDER, PHRASE_REVIEWER in it
        // call userRoleMapService.addRolesToUser() with parameters userRequest.userId and the array list
        // verify that userRoleMapRepository.findById(3)'s results match those in the array list
    }
}
