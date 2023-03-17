package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.entities.User;

import java.util.Optional;

public interface UserService {
	public Optional<User> createNewUser(UserRequest req, String preferredContactMethod);

	public Optional<User> find(String query);
	public Optional<User> findById(Long id);
	Optional<User> update(UserRequest request);

	User changePassword(String pw, String phoneNumber, String challengeCode);
	
}
