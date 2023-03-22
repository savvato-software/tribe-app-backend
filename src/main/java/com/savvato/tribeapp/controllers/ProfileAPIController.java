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
public class ProfileAPIController {

	@Autowired
    ProfileService profileService;
	// providing an instance
	// don't worry about how it is created
	// injects dependency into your code
	
	ProfileAPIController() {
			
	}
	
	@RequestMapping(value = { "/api/profile/{profileId}" }, method=RequestMethod.GET)
	public ResponseEntity<ProfileDTO> getById(@PathVariable Long profileId) {
		
		Optional<ProfileDTO> opt = profileService.getByUserId(profileId);

		if (opt.isPresent())
			return ResponseEntity.status(HttpStatus.OK).body(opt.get());
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value = { "/api/profile/{profileId}" }, method=RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody @Valid ProfileRequest request) {
		boolean rtn = profileService.update(request.userId, request.name, request.email, request.phone);
		
		if (rtn) {
			return ResponseEntity.status(HttpStatus.OK).body(rtn);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rtn);
		}
	}
}

