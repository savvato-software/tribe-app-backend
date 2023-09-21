package com.savvato.tribeapp.controllers;

import java.util.Date;

import javax.validation.Valid;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.AuthRequest;
import com.savvato.tribeapp.dto.UserDTO;
import com.savvato.tribeapp.entities.User;
import com.savvato.tribeapp.services.AuthService;
import com.savvato.tribeapp.services.AuthServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/public")
public class AuthAPIController {

    private final AuthenticationManager authenticationManager;

    public AuthAPIController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping( "/login" )
    public ResponseEntity<User> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.email, request.password
                    )
                );

            User user = ((UserPrincipal) authenticate.getPrincipal()).getUser();
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .password(user.getPassword())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .enabled(user.getEnabled())
                    .created(user.getCreated().toString())
                    .lastUpdated(user.getLastUpdated().toString())
                    .build();

            return ResponseEntity.ok()
                .header(
                    HttpHeaders.AUTHORIZATION,
                    AuthServiceImpl.generateAccessToken(userDTO)
                )
                .body(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
}
