//user role serviceimpl

package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.UserRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.savvato.tribeapp.entities.UserRole;
import com.savvato.tribeapp.repositories.UserRoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	@Autowired
	UserRoleRepository userRoleRepo;

	public Iterable<UserRoleDTO> getRoles() {
		Iterable<UserRole> userRoles = userRoleRepo.findAll();
		List<UserRoleDTO> rtn = new ArrayList<>();
		for (UserRole userRole : userRoles) {
			UserRoleDTO userRoleDTO = UserRoleDTO.builder()
					.name(userRole.getName())
					.build();
			rtn.add(userRoleDTO);
		}
		return rtn;
	}
}
