package main.service.jwt;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.Context;

@Service
public class AuthTokenService {

    private final JwtService jwtService;

    public AuthTokenService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateJwtTokenForCurrentUser(){
        var authenticate = SecurityContextHolder.getContext().getAuthentication();

        if (authenticate == null || !authenticate.isAuthenticated()) throw new IllegalStateException();

        Object principal =authenticate.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) throw new IllegalStateException();

        return jwtService.generateToken(userDetails);
    }
}
