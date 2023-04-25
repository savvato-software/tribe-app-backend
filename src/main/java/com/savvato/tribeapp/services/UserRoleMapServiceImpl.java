package com.savvato.tribeapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.savvato.tribeapp.entities.UserRoleMap;
import com.savvato.tribeapp.repositories.UserRoleMapRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleMapServiceImpl implements UserRoleMapService {
	@Autowired
	UserRoleMapRepository userRoleMapRepo;

	public List<String> getRoles() {
		ArrayList<String> rtn = new ArrayList<>();

		for (ROLES role : ROLES.values()) {
			rtn.add(role.name());
		}

		return rtn;
	}

	public void addRoleToUser(Long userId, ROLES role) {
		userRoleMapRepo.save(new UserRoleMap(userId, Long.valueOf(role.ordinal()+1+"") ));
	}
	
	public void removeRoleFromUser(Long userId, ROLES role) {
		userRoleMapRepo.delete(new UserRoleMap(userId, Long.valueOf(role.ordinal()+1+"") ));
	}

	public boolean addRolesToUser(Long userId, ArrayList<String> rolesToAdd) {
		boolean successfulAdd = true;
		for (String role : rolesToAdd) {
			try {
				addRoleToUser(userId, ROLES.valueOf(role));
			} catch (Exception e) {
				successfulAdd =  false;
			}
		}
		return successfulAdd;
	}

	public boolean removeRolesFromUser(Long userId, ArrayList<String> rolesToDelete) {
		boolean successfulDelete = true;
		for (String role : rolesToDelete) {
			if (role != "ACCOUNTHOLDER") {
				try {
					removeRoleFromUser(userId, ROLES.valueOf(role));
				} catch (Exception e) {
					successfulDelete =  false;
				}
			} else {
				successfulDelete = false;
			}
		}
		return successfulDelete;
	}
}
