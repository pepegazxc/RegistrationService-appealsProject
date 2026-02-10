package main.service.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthTokenService {

    private final JwtService jwtService;

    public AuthTokenService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateJwtTokenForCurrentUser(){
        var authenticate = SecurityContextHolder.getContext().getAuthentication();

        if (authenticate == null || !authenticate.isAuthenticated()) {
            log.error("Security Context is empty. JWT cannot be generate");
            throw new IllegalStateException();
        }

        Object principal =authenticate.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)){
            log.error("Principal is not supported={}", principal.getClass().getName());
            throw new IllegalStateException();
        }

        try {
            String token = jwtService.generateToken(userDetails);
            log.info("Generate new JWT. User identifier={}", userDetails.getUsername());
            return token;
        }catch (Exception e){
            log.error("Couldn't generate JWT. User identifier={}, {} ", userDetails.getUsername(), e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
