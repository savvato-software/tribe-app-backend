package com.savvato.tribeapp.services;


import com.savvato.tribeapp.dto.UserRoleDTO;
import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class UserRoleImplTest extends AbstractServiceImplTest {

	@TestConfiguration
	static class UserServiceTestContextConfiguration {
		@Bean
		public UserRoleService userRoleService() {
			return new UserRoleServiceImpl();
		}
	}

	@Autowired
	private UserRoleService userRoleService;

	@MockBean
	private UserRoleRepository userRoleRepository;

	@Test
	public void testGetRolesFindsListOfTestRoles() {
		UserRole userRole1 = new UserRole("ROLE_Admin");
		UserRole userRole2 = new UserRole("ROLE_accountholder");
		UserRole userRole3 = new UserRole("ROLE_phrasereviewer");

		List<UserRole> userRoles = new ArrayList<>();
		userRoles.add(userRole1);
		userRoles.add(userRole2);
		userRoles.add(userRole3);

		List<UserRoleDTO> userRoleDTOs = new ArrayList<>();
		for(UserRole userRole : userRoles) {
			UserRoleDTO userRoleDTO = UserRoleDTO.builder()
					.name(userRole.getName())
					.build();
			userRoleDTOs.add(userRoleDTO);
		}

		Mockito.when(userRoleRepository.findAll()).thenReturn(userRoles);

		List<UserRoleDTO> rtn = userRoleService.getRoles();
		assertEquals(rtn.size(), userRoles.size());
		for(int i=0; i<rtn.size(); i++){
			assertEquals(rtn.get(i).name, userRoleDTOs.get(i).name);
		}
	}
}
