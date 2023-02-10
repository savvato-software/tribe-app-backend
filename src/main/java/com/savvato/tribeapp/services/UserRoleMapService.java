package com.savvato.tribeapp.services;

import java.util.ArrayList;

public interface UserRoleMapService {
	
	enum ROLES { ADMIN, ACCOUNTHOLDER, PHRASE_REVIEWER }
	
	public void addRoleToUser(Long userId, ROLES role);
	public void removeRoleFromUser(Long userId, ROLES role);
	public boolean addRolesToUser(Long userId, ArrayList<String> rolesToAdd);
	public boolean removeRolesFromUser(Long userId, ArrayList<String> rolesToDelete);

}
