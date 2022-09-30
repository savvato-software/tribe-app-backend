package com.savvato.tribeapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.repositories.UserRepository;

@Service
public class UserPrincipalServiceImpl implements UserPrincipalService {

	@Autowired
	UserRepository userRepo;
	
	public UserPrincipal getUserPrincipalByName(String name) {
		Optional<User> opt = userRepo.findByName(name);
		
		if (opt.isPresent())
			return new UserPrincipal(opt.get());
		else
			return null;
	}
	
	public UserPrincipal getUserPrincipalByEmail(String email) {
		Optional<User> opt = userRepo.findByEmail(email);
		
		if (opt.isPresent())
			return new UserPrincipal(opt.get());
		else
			return null;
	}
}
