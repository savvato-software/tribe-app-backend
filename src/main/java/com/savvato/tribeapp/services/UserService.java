package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.dto.UserDTO;
import com.savvato.tribeapp.dto.UsernameDTO;
import com.savvato.tribeapp.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
	Long getLoggedInUserId();

	public Optional<User> createNewUser(UserRequest req, String preferredContactMethod);

	public Optional<User> find(String query);
	public Optional<User> findById(Long id);
	Optional<User> update(UserRequest request);

	UserDTO changePassword(String pw, String phoneNumber, String challengeCode);

	List<UserDTO> getAllUsers();

	UsernameDTO getUsernameDTO(Long userId);
}
