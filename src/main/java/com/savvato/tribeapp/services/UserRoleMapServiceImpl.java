package com.savvato.tribeapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.savvato.tribeapp.entities.UserRoleMap;
import com.savvato.tribeapp.repositories.UserRepository;
import com.savvato.tribeapp.repositories.UserRoleMapRepository;

import java.util.ArrayList;
import java.util.EnumSet;

@Service
public class UserRoleMapServiceImpl implements UserRoleMapService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserRoleMapRepository userRoleMapRepo;
	
	public void addRoleToUser(Long userId, ROLES role) {
		userRoleMapRepo.save(new UserRoleMap(userId, Long.valueOf(role.ordinal()+1+"") ));
	}
	
	public void removeRoleFromUser(Long userId, ROLES role) {
		userRoleMapRepo.delete(new UserRoleMap(userId, Long.valueOf(role.ordinal()+1+"") ));
	}

	public boolean addRolesToUser(Long userId, ArrayList<String> roles) {

		for (String role : roles) {
			try {
				addRoleToUser(userId, ROLES.valueOf(role));
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
