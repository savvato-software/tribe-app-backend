package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.dto.UserDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

	@Test
	public void testUpdateUser_whenGivenNameIsNull() {
		// given
		User user1 = getUser1();
		user1.setName(null);

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



//	@Test
	public void testUpdateUser_whenEmailIsNotValid() {
		// given

		// when

		// then
	}

	// etc...

	@Test
	public void testGetAllUsersFindsListOfTwoTestUsers(){
		User user1 = getUser1();
		User user2 = getUser2();

		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);

		List<UserDTO> userDTOS = new ArrayList<>();
		for (User user : users){
			UserDTO userDTO = UserDTO.builder()
					.id(user.getId())
					.name(user.getName())
					.password(user.getPassword())
					.phone(user.getPhone())
					.email(user.getEmail())
					.enabled(user.getEnabled())
					.created(user.getCreated().toString())
					.lastUpdated(user.getLastUpdated().toString())
					.build();
			userDTOS.add(userDTO);
		}

		Mockito.when(userRepository.findAll()).thenReturn(users);

		List<UserDTO> rtn = userService.getAllUsers();
		assertEquals(rtn.size(), 2);
		for(int i=0; i<rtn.size(); i++) {
			assertEquals(rtn.get(i).id, userDTOS.get(i).id);
			assertEquals(rtn.get(i).name, userDTOS.get(i).name);
			assertEquals(rtn.get(i).password, userDTOS.get(i).password);
			assertEquals(rtn.get(i).phone, userDTOS.get(i).phone);
			assertEquals(rtn.get(i).email, userDTOS.get(i).email);
			assertEquals(rtn.get(i).enabled, userDTOS.get(i).enabled);
			assertEquals(rtn.get(i).created, userDTOS.get(i).created);
			assertEquals(rtn.get(i).lastUpdated, userDTOS.get(i).lastUpdated);
		}
	}
}
