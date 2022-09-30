package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.ProfileDTO;

import java.util.Optional;

public interface ProfileService {
	Optional<ProfileDTO> getByUserId(Long id);

	boolean update(Long userId, String name, String email, String phone);
}

