package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import com.savvato.tribeapp.entities.User;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
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
	public void testUpdateUser_whenUsernameBelongsToExistingUser() {
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

	//	@Test
	public void testUpdateUser_whenPhoneIsNotValid() {
		// given

		// when

		// then
	}

//	@Test
	public void testUpdateUser_whenEmailIsNotValid() {
		// given

		// when

		// then
	}

	// etc...
}
