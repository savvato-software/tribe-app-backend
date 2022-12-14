package com.savvato.tribeapp.controllers;

import java.util.Date;

import javax.validation.Valid;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.AuthRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController 
public class AuthAPIController {

    private final AuthenticationManager authenticationManager;

    public AuthAPIController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = { "/api/public/login" }, method = RequestMethod.POST)
    public ResponseEntity<User> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.email, request.password
                    )
                );

            User user = ((UserPrincipal) authenticate.getPrincipal()).getUser();

            return ResponseEntity.ok()
                .header(
                    HttpHeaders.AUTHORIZATION,
                    AuthServiceImpl.generateAccessToken(user)
                )
                .body(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
}
