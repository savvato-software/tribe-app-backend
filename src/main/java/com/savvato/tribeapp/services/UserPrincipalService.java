package com.savvato.tribeapp.services;

import com.savvato.tribeapp.config.principal.UserPrincipal;

public interface UserPrincipalService {
	public UserPrincipal getUserPrincipalByName(String name);
	public UserPrincipal getUserPrincipalByEmail(String email);
}
