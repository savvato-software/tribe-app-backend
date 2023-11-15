package com.savvato.tribeapp.services;

import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public final class AuthServiceImpl {
    private AuthServiceImpl() {
    }

    public static String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer(Constants.JWT_ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET)
                .compact();
    }
}
