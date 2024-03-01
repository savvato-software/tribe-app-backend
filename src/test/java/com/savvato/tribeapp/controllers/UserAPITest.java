package com.savvato.tribeapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.ChangePasswordRequest;
import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.dto.UserDTO;
import com.savvato.tribeapp.dto.UserRoleDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.UserRepository;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAPIController.class)
public class UserAPITest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Gson gson;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserDetailsServiceTRIBEAPP userDetailsServiceTRIBEAPP;

    @MockBean
    private UserPrincipalService userPrincipalService;

    @MockBean
    private UserService userService;
    @MockBean
    private ProfileService profileService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SMSChallengeCodeService smsccs;

    @Captor
    ArgumentCaptor<UserRequest> userRequestCaptor;

    @Captor
    ArgumentCaptor<String> preferredContactMethodCaptor;

    @Captor
    ArgumentCaptor<String> availabilityQueryCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(springSecurity())
                        .build();
    }

    @Test
    public void createUserHappyPath() throws Exception {
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);
        rolesSet.add(UserRole.ROLE_PHRASEREVIEWER);

        User user = new User();
        user.setId(1L);
        user.setName(Constants.FAKE_USER_NAME1);
        user.setPassword("admin");
        user.setEnabled(1);
        user.setPhone("1234567890");
        user.setRoles(rolesSet);
        user.setCreated();
        user.setLastUpdated();
        user.setEmail(Constants.FAKE_USER_EMAIL1);

        UserRequest userRequest = new UserRequest();
        userRequest.id = user.getId();
        userRequest.name = user.getName();
        userRequest.phone = user.getPhone();
        userRequest.email = user.getEmail();
        userRequest.password = user.getPassword();
        Optional<User> userOpt = Optional.of(user);

        when(userService.createNewUser(any(UserRequest.class), anyString())).thenReturn(userOpt);
        MvcResult result =
                this.mockMvc
                        .perform(
                                post("/api/public/user/new")
                                        .content(gson.toJson(userRequest))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
        verify(userService, times(1))
                .createNewUser(userRequestCaptor.capture(), preferredContactMethodCaptor.capture());
        assertThat(userRequestCaptor.getValue()).usingRecursiveComparison().isEqualTo(userRequest);
        // using ObjectMapper instead of Gson, because Gson throws error when parsing timestamps of
        // created & lastUpdated
        User actualUser = mapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(actualUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void createUserWhenErrorThrown() throws Exception {
        Set<UserRole> rolesSet = new HashSet<>();
        rolesSet.add(UserRole.ROLE_ACCOUNTHOLDER);
        rolesSet.add(UserRole.ROLE_ADMIN);
        rolesSet.add(UserRole.ROLE_PHRASEREVIEWER);

        UserRequest userRequest = new UserRequest();
        userRequest.id = null;
        userRequest.name = null;
        userRequest.phone = Constants.FAKE_USER_PHONE2;
        userRequest.email = Constants.FAKE_USER_EMAIL2;
        userRequest.password = null;
        String errorMessage = "Missing critical UserRequest values.";
        when(userService.createNewUser(any(UserRequest.class), anyString()))
                .thenThrow(new IllegalArgumentException(errorMessage));

        this.mockMvc
                .perform(
                        post("/api/public/user/new")
                                .content(gson.toJson(userRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage))
                .andReturn();
        verify(userService, times(1))
                .createNewUser(userRequestCaptor.capture(), preferredContactMethodCaptor.capture());
        assertThat(userRequestCaptor.getValue()).usingRecursiveComparison().isEqualTo(userRequest);
    }

    @Test
    public void isUsernameAvailable() throws Exception {
        String username = "admin";
        when(userRepository.findByName(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new User()));
        // when userRepository returns empty Optional
        this.mockMvc
                .perform(
                        get("/api/public/user/isUsernameAvailable")
                                .param("q", username)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
        // when userRepository returns a User
        this.mockMvc
                .perform(
                        get("/api/public/user/isUsernameAvailable")
                                .param("q", username)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();
        verify(userRepository, times(2)).findByName(availabilityQueryCaptor.capture());
        assertEquals(availabilityQueryCaptor.getAllValues().get(0), username);
        assertEquals(availabilityQueryCaptor.getAllValues().get(1), username);
    }

    @Test
    public void isEmailAddressAvailable() throws Exception {
        String email = Constants.FAKE_USER_EMAIL2;
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new User()));

        // when userRepository returns empty Optional
        this.mockMvc
                .perform(
                        get("/api/public/user/isEmailAddressAvailable")
                                .param("q", email)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();

        // when userRepository returns a User
        this.mockMvc
                .perform(
                        get("/api/public/user/isEmailAddressAvailable")
                                .param("q", email)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();

        verify(userRepository, times(2)).findByEmail(availabilityQueryCaptor.capture());
        assertEquals(availabilityQueryCaptor.getAllValues().get(0), email);
        assertEquals(availabilityQueryCaptor.getAllValues().get(1), email);
    }

    @Test
    public void isPhoneNumberAvailable() throws Exception {
        String phone = Constants.FAKE_USER_PHONE2;
        when(userRepository.findByPhone(anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new ArrayList<>()))
                .thenReturn(Optional.of(List.of(new User())));

        // when userRepository returns an empty Optional
        this.mockMvc
                .perform(
                        get("/api/public/user/isPhoneNumberAvailable")
                                .param("q", phone)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();

        // when userRepository returns an empty list
        this.mockMvc
                .perform(
                        get("/api/public/user/isPhoneNumberAvailable")
                                .param("q", phone)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();

        // when userRepository returns a list containing a User
        this.mockMvc
                .perform(
                        get("/api/public/user/isPhoneNumberAvailable")
                                .param("q", phone)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();
    }

    @Test
    public void isUserInformationUniqueWhenUsernameTaken() throws Exception {
        String email = Constants.FAKE_USER_EMAIL2;
        String phone = Constants.FAKE_USER_PHONE2;
        String username = Constants.FAKE_USER_NAME2;
        String password = "admin";
        when(userRepository.findByName(anyString()))
                .thenReturn(Optional.of(new User(username, password, phone, email)));

        this.mockMvc
                .perform(
                        get("/api/public/user/isUserInformationUnique")
                                .param("name", username)
                                .param("phone", phone)
                                .param("email", email)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"response\": \"username\"}"))
                .andReturn();
    }

    @Test
    public void isUserInformationUniqueWhenEmailTaken() throws Exception {
        String email = Constants.FAKE_USER_EMAIL2;
        String phone = Constants.FAKE_USER_PHONE2;
        String username = Constants.FAKE_USER_NAME2;
        String password = "admin";
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(new User(username, password, phone, email)));

        this.mockMvc
                .perform(
                        get("/api/public/user/isUserInformationUnique")
                                .param("name", username)
                                .param("phone", phone)
                                .param("email", email)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"response\": \"email\"}"))
                .andReturn();
    }

    @Test
    public void isUserInformationUniqueWhenPhoneTaken() throws Exception {
        String email = Constants.FAKE_USER_EMAIL2;
        String phone = Constants.FAKE_USER_PHONE2;
        String username = Constants.FAKE_USER_NAME2;
        String password = "admin";
        User user = new User(username, password, phone, email);
        when(userRepository.findByPhone(anyString())).thenReturn(Optional.of(List.of(user)));

        this.mockMvc
                .perform(
                        get("/api/public/user/isUserInformationUnique")
                                .param("name", username)
                                .param("phone", phone)
                                .param("email", email)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"response\": \"phone\"}"))
                .andReturn();
    }

    @Test
    public void isUserInformationUniqueHappyPath() throws Exception {
        String email = Constants.FAKE_USER_EMAIL2;
        String phone = Constants.FAKE_USER_PHONE2;
        String username = Constants.FAKE_USER_NAME2;
        String password = "admin";
        User user = new User(username, password, phone, email);

        this.mockMvc
                .perform(
                        get("/api/public/user/isUserInformationUnique")
                                .param("name", username)
                                .param("phone", phone)
                                .param("email", email)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"response\": true}"))
                .andReturn();
    }

    @Test
    public void changePassword() throws Exception {
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
        user.setRoles(Set.of(UserRole.ROLE_ACCOUNTHOLDER));
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.phoneNumber = Constants.FAKE_USER_PHONE2;
        changePasswordRequest.pw = "admin";
        changePasswordRequest.smsChallengeCode = "ABCDEF";
        UserDTO expectedUserDTO =
                UserDTO.builder()
                        .id(Constants.NULL_VALUE_ID)
                        .name(Constants.FAKE_USER_NAME2)
                        .password("not an admin")
                        .phone(changePasswordRequest.phoneNumber)
                        .email(Constants.FAKE_USER_EMAIL2)
                        .enabled(1)
                        .roles(getUserRoleDTO(user))
                        .build();
        when(userService.changePassword(anyString(), anyString(), anyString())).thenReturn(expectedUserDTO);

        MvcResult result = this.mockMvc
                .perform(
                        post("/api/public/user/changePassword")
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8")
                                .content(gson.toJson(changePasswordRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Type userDTOType = new TypeToken<UserDTO>() {
        }.getType();
        UserDTO actualUserDTO = gson.fromJson(result.getResponse().getContentAsString(), userDTOType);
        assertThat(actualUserDTO).usingRecursiveComparison().isEqualTo(expectedUserDTO);
    }

    private List <UserRoleDTO> getUserRoleDTO(User user){
        Set<UserRole> userRole = user.getRoles();
        List<UserRoleDTO> rtn = new ArrayList<>();
        Iterator<UserRole> iterator  = userRole.iterator();
        while (iterator.hasNext()){
            UserRole userRoles = iterator.next();
            Long id = userRoles.getId();
            String name = userRoles.getName();
            UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                    .id(id)
                    .name(name)
                    .build();
            rtn.add(userRoleDTO);
        }
        return rtn;
    }
}
