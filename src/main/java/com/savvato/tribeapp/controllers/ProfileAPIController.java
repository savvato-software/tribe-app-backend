package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.annotations.controllers.ProfileAPIController.GetById;
import com.savvato.tribeapp.controllers.annotations.controllers.ProfileAPIController.Update;
import com.savvato.tribeapp.controllers.dto.ProfileRequest;
import com.savvato.tribeapp.dto.GenericMessageDTO;
import com.savvato.tribeapp.dto.ProfileDTO;
import com.savvato.tribeapp.services.GenericMessageService;
import com.savvato.tribeapp.services.ProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Tag(name="profile", description = "User profile details")
@RequestMapping("/api/profile/{profileId}")
public class ProfileAPIController {

	@Autowired
    ProfileService profileService;

	@Autowired private GenericMessageService genericMessageService;

	ProfileAPIController() {
			
	}
	
	@GetById
	@GetMapping
	public ResponseEntity<ProfileDTO> getById(@Parameter(description = "The profile ID", example = "1") @PathVariable Long profileId) {
		
		Optional<ProfileDTO> opt = profileService.getByUserId(profileId);

		if (opt.isPresent())
			//return ResponseEntity.status(HttpStatus.OK).body(opt.get());
			return ResponseEntity.ok(opt.get());
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	@Update
	@PutMapping
	public ResponseEntity<GenericMessageDTO> update(@RequestBody @Valid ProfileRequest request) {
		boolean val = profileService.update(request.userId, request.name, request.email, request.phone);
		GenericMessageDTO rtn =  genericMessageService.createDTO(val);
		if (val) {
			return ResponseEntity.status(HttpStatus.OK).body(rtn);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rtn);
		}
	}
}

