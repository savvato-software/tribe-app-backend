package com.savvato.tribeapp.controllers;

import java.util.Date;

import javax.validation.Valid;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.controllers.dto.AuthRequest;
import com.savvato.tribeapp.entities.User;
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
                    generateAccessToken(user)
                )
                .body(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer(Constants.JWT_ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET)
                .compact();
    }
}
