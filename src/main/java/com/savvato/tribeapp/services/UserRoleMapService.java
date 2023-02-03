package com.savvato.tribeapp.services;

public interface UserRoleMapService {
	
	enum ROLES { ADMIN, ACCOUNTHOLDER, PHRASE_REVIEWER }
	
	public void addRoleToUser(Long userId, ROLES role);
	public void removeRoleFromUser(Long userId, ROLES role);
}
