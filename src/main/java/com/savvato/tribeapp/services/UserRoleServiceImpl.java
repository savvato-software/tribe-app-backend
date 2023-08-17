//user role serviceimpl

package com.savvato.tribeapp.services;

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

	public Iterable<UserRole> getRoles() {
		return userRoleRepo.findAll();
	}
}
