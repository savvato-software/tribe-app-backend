package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.ProfileRequest;
import com.savvato.tribeapp.dto.ProfileDTO;
import com.savvato.tribeapp.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile/{profileId}")
public class ProfileAPIController {

	@Autowired
    ProfileService profileService;

	ProfileAPIController() {
			
	}
	
	@GetMapping
	public ResponseEntity<ProfileDTO> getById(@PathVariable Long profileId) {
		
		Optional<ProfileDTO> opt = profileService.getByUserId(profileId);

		if (opt.isPresent())
			return ResponseEntity.status(HttpStatus.OK).body(opt.get());
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@PutMapping
	public ResponseEntity<Boolean> update(@RequestBody @Valid ProfileRequest request) {
		boolean rtn = profileService.update(request.userId, request.name, request.email, request.phone);
		
		if (rtn) {
			return ResponseEntity.status(HttpStatus.OK).body(rtn);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rtn);
		}
	}
}

