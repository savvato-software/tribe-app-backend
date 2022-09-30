package com.savvato.tribeapp.services;

import java.util.Optional;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceTRIBEAPP implements UserDetailsService {

	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> opt = userRepo.findByEmail(email);
		User rtn = null;

		if (opt.isPresent())
			rtn = opt.get();
		else
			throw new UsernameNotFoundException(email);

		return new UserPrincipal(rtn);
	}
}
