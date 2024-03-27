package com.savvato.tribeapp.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.PermissionsRequest;
import com.savvato.tribeapp.dto.UserDTO;
import com.savvato.tribeapp.dto.GenericResponseDTO;
import com.savvato.tribeapp.dto.UserRoleDTO;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PermissionsAPIController.class)
public class PermissionsAPITest {
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
    private GenericResponseService GenericResponseService;

    @MockBean
    private UserService userService;
    @MockBean
    private UserRoleMapService userRoleMapService;
    @MockBean
    private UserRoleService userRoleService;
    @Captor
    private ArgumentCaptor<ArrayList<String>> permissionsCaptor;
    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

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
    public void getAllUsersWhenFound() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        UserDTO userDTO =
                UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .roles(getUserRoleDTOSet(user))
                        .password(user.getPassword())
                        .created(user.getCreated().toString())
                        .lastUpdated(user.getLastUpdated().toString())
                        .enabled(user.getEnabled())
                        .build();
        List<UserDTO> expectedUserList = List.of(userDTO);
        when(userService.getAllUsers()).thenReturn(expectedUserList);
        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/permissions/users")
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        Type userDTOListType = new TypeToken<List<UserDTO>>() {
        }.getType();

        List<UserDTO> actualUserList =
                gson.fromJson(result.getResponse().getContentAsString(), userDTOListType);
        assertThat(actualUserList).usingRecursiveComparison().isEqualTo(expectedUserList);
    }

    private Set<UserRoleDTO> getUserRoleDTOSet(User user) {
        Set<UserRole> userRole = user.getRoles();
        Set<UserRoleDTO> rtn = new HashSet<>();
        Iterator<UserRole> iterator = userRole.iterator();
        while (iterator.hasNext()) {
            UserRole ur = iterator.next();
            Long id = ur.getId();
            String name = ur.getName();
            UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                    .id(id)
                    .name(name)
                    .build();
            rtn.add(userRoleDTO);
        }
        return rtn;
    }

    @Test
    public void getAllUsersWhenNoneFound() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        when(userService.getAllUsers()).thenReturn(null);
        this.mockMvc
                .perform(
                        get("/api/permissions/users")
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").doesNotExist()) // ensure body is empty
                .andReturn();
    }

    @Test
    public void getAllUserRoleNamesWhenFound() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        List<String> expectedRoleNamesList =
                List.of(
                        UserRole.ROLE_ACCOUNTHOLDER.getName(),
                        UserRole.ROLE_ADMIN.getName(),
                        UserRole.ROLE_PHRASEREVIEWER.getName());
        when(userRoleMapService.getRoles()).thenReturn(expectedRoleNamesList);
        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/permissions/user-roles")
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        Type userRoleNameListType = new TypeToken<List<String>>() {
        }.getType();

        List<UserDTO> actualRoleNamesList =
                gson.fromJson(result.getResponse().getContentAsString(), userRoleNameListType);
        assertThat(actualRoleNamesList).usingRecursiveComparison().isEqualTo(expectedRoleNamesList);
    }

    @Test
    public void getAllUserRoleNamesWhenNoneFound() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        when(userRoleMapService.getRoles()).thenReturn(null);
        this.mockMvc
                .perform(
                        get("/api/permissions/user-roles")
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").doesNotExist()) // ensure body is empty
                .andReturn();
    }

//    @Test
    public void getAllUserRolesWhenFound() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        List<UserRole> expectedRolesList =
                List.of(UserRole.ROLE_ACCOUNTHOLDER, UserRole.ROLE_ADMIN, UserRole.ROLE_PHRASEREVIEWER);

        // TODO: userRoleService.getRoles() should be returning a list of UserRoleDTOs, not UserRoles

//        when(userRoleService.getRoles()).thenReturn(expectedRolesList);
        MvcResult result =
                this.mockMvc
                        .perform(
                                get("/api/permissions/user-roles-list")
                                        .header("Authorization", "Bearer " + auth)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        Type userRoleListType = new TypeToken<List<UserRole>>() {
        }.getType();

        List<UserDTO> actualRolesList =
                gson.fromJson(result.getResponse().getContentAsString(), userRoleListType);
        assertThat(actualRolesList).usingRecursiveComparison().isEqualTo(expectedRolesList);
    }

    @Test
    public void getAllUserRolesWhenNoneFound() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        when(userRoleService.getRoles()).thenReturn(null);
        this.mockMvc
                .perform(
                        get("/api/permissions/user-roles-list")
                                .header("Authorization", "Bearer " + auth)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$").doesNotExist()) // ensure body is empty
                .andReturn();
    }

    @Test
    public void addPermissionsWhenSuccessful() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ArrayList<String> permissions =
                new ArrayList<>(
                        List.of(UserRole.ROLE_ACCOUNTHOLDER.getName(), UserRole.ROLE_ADMIN.getName()));
        PermissionsRequest permissionsRequest = new PermissionsRequest();
        permissionsRequest.id = user.getId();
        permissionsRequest.permissions = permissions;

        when(userRoleMapService.addRolesToUser(anyLong(), any())).thenReturn(true);
        when(GenericResponseService.createDTO(
                anyBoolean()))
                .thenReturn(GenericResponseDTO.builder()
                        .booleanMessage(true)
                        .build());
        this.mockMvc
                .perform(
                        post("/api/permissions")
                                .header("Authorization", "Bearer " + auth)
                                .content(gson.toJson(permissionsRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("booleanMessage").value(true))
                .andReturn();


        verify(userRoleMapService, times(1))
                .addRolesToUser(userIdCaptor.capture(), permissionsCaptor.capture());
        assertEquals(userIdCaptor.getValue(), permissionsRequest.id);
        assertThat(permissionsCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(permissionsRequest.permissions);
    }

    @Test
    public void addPermissionsWhenUnsuccessful() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ArrayList<String> permissions = new ArrayList<>(List.of("NONEXISTENT_ROLE"));
        PermissionsRequest permissionsRequest = new PermissionsRequest();
        permissionsRequest.id = user.getId();
        permissionsRequest.permissions = permissions;

        when(userRoleMapService.addRolesToUser(anyLong(), any())).thenReturn(false);
        when(GenericResponseService.createDTO(
                anyBoolean()))
                .thenReturn(GenericResponseDTO.builder()
                        .booleanMessage(false)
                        .build());
        this.mockMvc
                .perform(
                        post("/api/permissions")
                                .header("Authorization", "Bearer " + auth)
                                .content(gson.toJson(permissionsRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("booleanMessage").value(false))
                .andReturn();

        verify(userRoleMapService, times(1))
                .addRolesToUser(userIdCaptor.capture(), permissionsCaptor.capture());
        assertEquals(userIdCaptor.getValue(), permissionsRequest.id);
        assertThat(permissionsCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(permissionsRequest.permissions);
    }

    @Test
    public void deletePermissionsWhenSuccessful() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ArrayList<String> permissions =
                new ArrayList<>(
                        List.of(UserRole.ROLE_ACCOUNTHOLDER.getName(), UserRole.ROLE_ADMIN.getName()));
        PermissionsRequest permissionsRequest = new PermissionsRequest();
        permissionsRequest.id = user.getId();
        permissionsRequest.permissions = permissions;

        when(userRoleMapService.removeRolesFromUser(anyLong(), any())).thenReturn(true);
        when(GenericResponseService.createDTO(
                anyBoolean()))
                .thenReturn(GenericResponseDTO.builder()
                        .booleanMessage(true)
                        .build());
        this.mockMvc
                .perform(
                        delete("/api/permissions")
                                .header("Authorization", "Bearer " + auth)
                                .content(gson.toJson(permissionsRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("booleanMessage").value(true))
                .andReturn();

        verify(userRoleMapService, times(1))
                .removeRolesFromUser(userIdCaptor.capture(), permissionsCaptor.capture());
        assertEquals(userIdCaptor.getValue(), permissionsRequest.id);
        assertThat(permissionsCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(permissionsRequest.permissions);
    }

    @Test
    public void deletePermissionsWhenUnsuccessful() throws Exception {
        Mockito.when(userPrincipalService.getUserPrincipalByEmail(Mockito.anyString()))
                .thenReturn(new UserPrincipal(user));
        String auth = AuthServiceImpl.generateAccessToken(user);
        ArrayList<String> permissions = new ArrayList<>(List.of("NONEXISTENT_ROLE"));
        PermissionsRequest permissionsRequest = new PermissionsRequest();
        permissionsRequest.id = user.getId();
        permissionsRequest.permissions = permissions;

        when(userRoleMapService.removeRolesFromUser(anyLong(), any())).thenReturn(false);
        when(GenericResponseService.createDTO(
                anyBoolean()))
                .thenReturn(GenericResponseDTO.builder()
                        .booleanMessage(false)
                        .build());
        this.mockMvc
                .perform(
                        delete("/api/permissions")
                                .header("Authorization", "Bearer " + auth)
                                .content(gson.toJson(permissionsRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("booleanMessage").value(false))
                .andReturn();

        verify(userRoleMapService, times(1))
                .removeRolesFromUser(userIdCaptor.capture(), permissionsCaptor.capture());
        assertEquals(userIdCaptor.getValue(), permissionsRequest.id);
        assertThat(permissionsCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(permissionsRequest.permissions);
    }
}
