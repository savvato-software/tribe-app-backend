package com.savvato.tribeapp.config.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.services.UserPrincipalService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private static final Log logger = LogFactory.getLog(JwtTokenFilter.class);
	
	@Autowired
    UserPrincipalService userPrincipalService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if ((header == null || header.equals("")) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        UserDetails userDetails = this.userPrincipalService.getUserPrincipalByEmail(getEmailAddress(token));

        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                    List.of() : userDetails.getAuthorities()
            );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
    
    // taken from: https://github.com/Yoh0xFF/java-spring-security-example/blob/master/src/main/java/io/example/configuration/security/JwtTokenUtil.java
    private boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature - " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token - " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token - " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token - " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty - " + ex.getMessage());
        }
        return false;
    }
    
    // taken from: https://github.com/Yoh0xFF/java-spring-security-example/blob/master/src/main/java/io/example/configuration/security/JwtTokenUtil.java
    public String getEmailAddress(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Constants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[1];
    }    
}
