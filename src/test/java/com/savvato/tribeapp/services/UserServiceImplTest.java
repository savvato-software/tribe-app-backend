package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.dto.UserDTO;
import com.savvato.tribeapp.dto.UserNameDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest extends AbstractServiceImplTest {

    @TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRoleMapService userRoleMapService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private SMSChallengeCodeService smsccs;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testCreateNewUser() {
        // given
        User user1 = getUser1();

        Mockito.when(userRepository.findByNamePhoneOrEmail(any(String.class), any(String.class), any(String.class)))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user1);

        UserRequest userRequest = getUserRequestFor(user1);

        // when
        userService.createNewUser(userRequest, USER1_PREFERRED_CONTACT_METHOD);

        // then
        ArgumentCaptor<User> arg1 = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(arg1.capture());
        assertThat(arg1.getValue().getName()).isEqualTo(userRequest.name);
        assertThat(arg1.getValue().getPassword()).isNotEqualTo(userRequest.password); // password is hashed
        assertThat(arg1.getValue().getEmail()).isEqualTo(userRequest.email);
        assertThat(arg1.getValue().getPhone()).isEqualTo(userRequest.phone);

        Set<UserRole> set = arg1.getValue().getRoles();
        assertThat(set).hasSize(1);
        assertThat(set.iterator().next().getName()).isEqualTo(UserRole.ROLE_ACCOUNTHOLDER.getName());
    }

    @Test
    public void testCreateUser_whenUsernameBelongsToExistingUser() {
        // given
        User user1 = getUser1();
        User user2 = getUser2();

        Mockito.when(userRepository.findByNamePhoneOrEmail(any(String.class), any(String.class), any(String.class)))
                .thenReturn(Optional.of(user2));

        // when
        boolean caughtException = false;

        try {
            userService.createNewUser(getUserRequestFor(user1), USER1_PREFERRED_CONTACT_METHOD);
        } catch (Exception e) {
            // then
            caughtException = true;
        }

        assertThat(caughtException).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a"})
    @NullSource
    public void createNewUserWhenNameInvalid(String name) {
        User user1 = getUser1();
        user1.setName(name);
        UserRequest userRequest = getUserRequestFor(user1);
        String preferredContactMethod = "email";

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createNewUser(userRequest, preferredContactMethod);
        }, "name");

        verify(userRepository, never()).findByNamePhoneOrEmail(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @ParameterizedTest
    @NullSource
    public void createNewUserWhenPhoneInvalid(String phone) {
        User user1 = getUser1();
        user1.setPhone(phone);
        UserRequest userRequest = getUserRequestFor(user1);
        String preferredContactMethod = "email";

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createNewUser(userRequest, preferredContactMethod);
        }, "phone");

        verify(userRepository, never()).findByNamePhoneOrEmail(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @ParameterizedTest
    @NullSource
    public void createNewUserWhenEmailInvalid(String email) {
        User user1 = getUser1();
        user1.setEmail(email);
        UserRequest userRequest = getUserRequestFor(user1);
        String preferredContactMethod = "email";

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createNewUser(userRequest, preferredContactMethod);
        }, "email");

        verify(userRepository, never()).findByNamePhoneOrEmail(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }


    @ParameterizedTest
    @NullAndEmptySource
    public void createNewUserWhenPasswordInvalid(String password) {
        User user1 = getUser1();
        user1.setPassword(password);
        UserRequest userRequest = getUserRequestFor(user1);
        String preferredContactMethod = "email";

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createNewUser(userRequest, preferredContactMethod);
        }, "password");

        verify(userRepository, never()).findByNamePhoneOrEmail(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_happyPath() {
        // given
        User user1 = getUser1();
        user1.setName("Barry Johnson");

        UserRequest userRequest = getUserRequestFor(user1);

        Mockito.when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user1));

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user1);

        // when
        Optional<User> rtn = userService.update(userRequest);

        // then
        assertThat(rtn != null && rtn instanceof Optional<User>);
        assertThat(rtn.isPresent()).isTrue();
        assertThat(rtn.get().getName().equals("Barry Johnson"));

        ArgumentCaptor<User> arg1 = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(arg1.capture());
        assertThat(arg1.getValue().getName()).isEqualTo(userRequest.name);
        // assertThat(arg1.getValue().getPassword()).isNotEqualTo(userRequest.password); // password should be hashed, but in our test, is not
        assertThat(arg1.getValue().getEmail()).isEqualTo(userRequest.email);
        assertThat(arg1.getValue().getPhone()).isEqualTo(userRequest.phone);
    }

    @Test
    public void testUpdateUser_whenGivenUserIdIsNotFound() {
        // given
        User user1 = getUser1();
        user1.setName("Barry Johnson");

        UserRequest userRequest = getUserRequestFor(user1);

        Mockito.when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user1);

        // when
        Optional<User> rtn = userService.update(userRequest);

        // then
        assertThat(rtn != null && rtn instanceof Optional<User>);
        assertThat(rtn.isPresent()).isFalse();

        ArgumentCaptor<User> arg1 = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(0)).save(arg1.capture());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a"})
    @NullSource
    public void testUpdateUser_whenGivenNameIsInvalid(String name) {
        // given
        User user1 = getUser1();
        user1.setName(name);

        UserRequest userRequest = getUserRequestFor(user1);

        // when
        boolean caughtException = false;

        try {
            userService.update(userRequest);
        } catch (IllegalArgumentException iae) {
            caughtException = true;
        }

        // then
        assertThat(caughtException).isTrue();
    }


    @ParameterizedTest
    @NullSource
    public void testUpdateUser_whenEmailIsNotValid(String email) {
        User user1 = getUser1();
        user1.setEmail(email);
        UserRequest userRequest = getUserRequestFor(user1);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.update(userRequest);
        }, "email");

        verify(userRepository, never()).findByNamePhoneOrEmail(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @ParameterizedTest
    @NullSource
    public void testUpdateUser_whenPhoneIsNotValid(String phone) {
        User user1 = getUser1();
        user1.setPhone(phone);
        UserRequest userRequest = getUserRequestFor(user1);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.update(userRequest);
        }, "phone");

        verify(userRepository, never()).findByNamePhoneOrEmail(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testGetAllUsersFindsListOfTwoTestUsers() {
        User user1 = getUser1();
        user1.setRoles(getUserRoles_Admin());
        User user2 = getUser2();
        user2.setRoles(getUserRoles_AccountHolder());

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(getUserDTO(user));
        }

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> rtn = userService.getAllUsers();
        assertEquals(rtn.size(), 2);
        for (int i = 0; i < rtn.size(); i++) {
            assertEquals(rtn.get(i).id, userDTOS.get(i).id);
            assertEquals(rtn.get(i).name, userDTOS.get(i).name);
            assertEquals(rtn.get(i).password, userDTOS.get(i).password);
            assertEquals(rtn.get(i).phone, userDTOS.get(i).phone);
            assertEquals(rtn.get(i).email, userDTOS.get(i).email);
            assertEquals(rtn.get(i).enabled, userDTOS.get(i).enabled);
            assertEquals(rtn.get(i).created, userDTOS.get(i).created);
            assertEquals(rtn.get(i).lastUpdated, userDTOS.get(i).lastUpdated);
            for (UserRole userRole : users.get(i).getRoles()) {
                assertTrue(rtn.get(i).roles.contains(userRole));
            }
        }
    }

    @Test
    public void find() {
        String email = USER1_EMAIL;
        Optional<User> expectedUser = Optional.of(getUser1());
        when(userRepository.findByPhoneOrEmail(anyString())).thenReturn(expectedUser);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);

        Optional<User> actualUser = userService.find(email);
        verify(userRepository, times(1)).findByPhoneOrEmail(emailCaptor.capture());
        assertEquals(email, emailCaptor.getValue());
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    public void findById() {
        Long id = USER1_ID;
        Optional<User> expectedUser = Optional.of(getUser1());
        when(userRepository.findById(anyLong())).thenReturn(expectedUser);
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

        Optional<User> actualUser = userService.findById(id);
        verify(userRepository, times(1)).findById(userIdCaptor.capture());
        assertEquals(id, userIdCaptor.getValue());
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    public void testChangePasswordHappyPath() {
        String password = "test";
        String phoneNumber = "3333333333";
        String smsChallengeCode = "code";

        User user = getUser1();
        user.setPassword(password);
        user.setPhone(phoneNumber);
        user.setRoles(getUserRoles_Admin());

        List users = new ArrayList<>();
        users.add(user);
        UserDTO userDTO = getUserDTO(user);

        Mockito.when(smsccs.isAValidSMSChallengeCode(any(String.class), any(String.class))).thenReturn(true);
        Mockito.when(userRepository.findByPhone(any(String.class))).thenReturn(Optional.of(users));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn(password);

        UserDTO rtn = userService.changePassword(password, phoneNumber, smsChallengeCode);
        assertEquals(rtn.id, userDTO.id);
        assertEquals(rtn.name, userDTO.name);
        assertEquals(rtn.password, userDTO.password);
        assertEquals(rtn.phone, userDTO.phone);
        assertEquals(rtn.email, userDTO.email);
        assertEquals(rtn.enabled, userDTO.enabled);
        assertEquals(rtn.created, userDTO.created);
        assertEquals(rtn.lastUpdated, userDTO.lastUpdated);
        for (UserRole userRole : user.getRoles()) {
            assertTrue(rtn.roles.contains(userRole));
        }
    }

    private UserDTO getUserDTO(User user) {
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .phone(user.getPhone())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .created(user.getCreated().toString())
                .lastUpdated(user.getLastUpdated().toString())
                .roles(user.getRoles())
                .build();

        return userDTO;
    }

    @Test
    public void getUserNameDTOHappyPath(){
        Long testId = 1L;

        User user = new User();
        user.setId(testId);
        user.setName("Marge");

        UserNameDTO expectedUserNameDTO = UserNameDTO.builder()
                .userId(testId)
                .userName("Marge")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserNameDTO userNameDTO = userService.getUserNameDTO(testId);
        assertEquals(userNameDTO.userId,expectedUserNameDTO.userId);
        assertEquals(userNameDTO.userName,expectedUserNameDTO.userName);
    }

}
