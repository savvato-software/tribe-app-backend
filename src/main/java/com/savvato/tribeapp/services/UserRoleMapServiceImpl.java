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
		userRoleMapRepo.save(new UserRoleMap(userId, Long.valueOf(role.ordinal()+"") ));
	}
	
	public void removeRoleFromUser(Long userId, ROLES role) {
		userRoleMapRepo.delete(new UserRoleMap(userId, Long.valueOf(role.ordinal()+"") ));
	}

	public boolean addRolesToUser(Long userId, ArrayList<String> roles) {
		EnumSet<ROLES> allRoles = EnumSet.allOf(ROLES.class);
		for (String role : roles) {
			if (allRoles.contains(role)) {
				addRoleToUser(userId, ROLES.valueOf(role));
			} else {
				roles.remove(role);
			}
		}
		if (roles.isEmpty()) {
			return true;
		}
		return false;
	}
}
