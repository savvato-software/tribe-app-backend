package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.annotations.controllers.UserAPIController.*;
import com.savvato.tribeapp.controllers.dto.ChangePasswordRequest;
import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.dto.UserDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.repositories.UserRepository;
import com.savvato.tribeapp.services.ProfileService;
import com.savvato.tribeapp.services.SMSChallengeCodeService;
import com.savvato.tribeapp.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/user")
@Tag(
    name = "public/user",
    description =
        "Publicly available user-information relevant paths, no login/credentials needed to make requests to them")
public class UserAPIController {

  @Autowired UserService userService;

  @Autowired ProfileService profileService;

  @Autowired UserRepository ur;

  @Autowired SMSChallengeCodeService smsccs;

  UserAPIController() {}

  @CreateNewUser
  @PostMapping("/new")
  public ResponseEntity createUser(@RequestBody @Valid UserRequest req) {

    try {
      Optional<User> response = userService.createNewUser(req, Constants.SMS);

      return new ResponseEntity<>(response.get(), HttpStatus.OK);
    } catch (IllegalArgumentException iae) {
      return new ResponseEntity(iae.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  // TODO: figure a way to have these enddpoints not be publicly accessible, but also not require a
  // user to be logged in.
  // 			a secret, but general purpose for-the-app login.

  // api/public/user/isUsernameAvailable?q=sample
  @IsUsernameAvailable
  @GetMapping("/isUsernameAvailable")
  public boolean isUsernameAvailable(
      @Parameter(description = "The username", example = "admin") @RequestParam("q")
          String queryStr) {
    return this.ur.findByName(queryStr).isPresent() == false;
  }

  // api/public/user/isPhoneNumberAvailable?q=7205870001
  @IsPhoneNumberAvailable
  @GetMapping("/isPhoneNumberAvailable")
  public boolean isPhoneNumberAvailable(
      @Parameter(description = "The phone number", example = "1234567890") @RequestParam("q")
          String queryStr) {
    Optional<List<User>> opt = this.ur.findByPhone(queryStr);

    if (opt.isPresent()) return opt.get().size() == 0;
    else return true;
  }

  // api/public/user/isEmailAddressAvailable?q=anAddress@domain.com
  @IsEmailAddressAvailable
  @GetMapping("/isEmailAddressAvailable")
  public boolean isEmailAddressAvailable(
      @Parameter(description = "The email address", example = "admin@tribeapp.com")
          @RequestParam("q")
          String queryStr) {
    return this.ur.findByEmail(queryStr).isPresent() == false;
  }

  // api/public/user/isUserInformationUnique?name=sample&phone=7205870001&email=anAddress@domain.com
  @IsUserInformationUnique
  @GetMapping("/isUserInformationUnique")
  public String isUserInformationUnique(
      @Parameter(description = "The username", example = "admin") @RequestParam("name")
          String username,
      @Parameter(description = "The phone number", example = "1234567890") @RequestParam("phone")
          String phone,
      @Parameter(description = "The email address", example = "admin@tribeapp.com")
          @RequestParam("email")
          String email) {
    if (!isUsernameAvailable(username)) return "{\"response\": \"username\"}";
    if (!isPhoneNumberAvailable(phone)) return "{\"response\": \"phone\"}";
    if (!isEmailAddressAvailable(email)) return "{\"response\": \"email\"}";

    return "{\"response\": true}";
  }

  @ChangePassword
  @PostMapping("/changePassword")
  public User changePassword(@RequestBody @Valid ChangePasswordRequest request) {
    return userService.changePassword(request.pw, request.phoneNumber, request.smsChallengeCode);
  }
}
