package com.savvato.tribeapp.services;

public interface UserRoleMapService {
	
	enum ROLES { ADMIN, ACCOUNTHOLDER }
	
	public void addRoleToUser(Long userId, ROLES role);
	public void removeRoleFromUser(Long userId, ROLES role);
}
