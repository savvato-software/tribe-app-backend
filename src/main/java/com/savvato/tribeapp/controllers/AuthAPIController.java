package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.controllers.annotations.controllers.AuthAPIController.Login;
import com.savvato.tribeapp.controllers.dto.AuthRequest;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.services.AuthServiceImpl;
import com.savvato.tribeapp.services.GenericMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
@Tag(
    name = "public",
    description = "Publicly available paths, no login/credentials needed to make requests to them")
public class AuthAPIController {

  private final AuthenticationManager authenticationManager;

  @Autowired private GenericMessageService genericMessageService;

  public AuthAPIController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Login
  @PostMapping("/login")
  public ResponseEntity<User> login(@RequestBody @Valid AuthRequest request) {
    try {
      Authentication authenticate =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.email, request.password));

      User user = ((UserPrincipal) authenticate.getPrincipal()).getUser();

      return ResponseEntity.ok()
          .header(HttpHeaders.AUTHORIZATION, AuthServiceImpl.generateAccessToken(user))
          .body(user);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
