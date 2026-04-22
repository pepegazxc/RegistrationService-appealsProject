package main.configuration.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.dto.response.SuccessLoginResponse;
import main.entity.UsersEntity;
import main.event.LoginEvent;
import main.exception.user.UserNotFoundException;
import main.repository.UserRepository;
import main.service.infrastructure.CipherService;
import main.service.infrastructure.jwt.AuthTokenService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthTokenService jwt;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;
    private final CipherService cipher;

    public JsonAuthenticationSuccessHandler(AuthTokenService jwt, ApplicationEventPublisher publisher, UserRepository userRepository, CipherService cipher) {
        this.jwt = jwt;
        this.publisher = publisher;
        this.userRepository = userRepository;
        this.cipher = cipher;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        SuccessLoginResponse loginResponse = new SuccessLoginResponse(
                "Success login",
                jwt.generateJwtTokenForCurrentUser()
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String identifier = user.getUsername();

        UsersEntity usersEntity = findUser(identifier);

        String email = decrypt(usersEntity.getCipherEmail());

        publisher.publishEvent(new LoginEvent(
                email,
                LocalDateTime.now()
        ));

        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
    }

    private UsersEntity findUser(String identifier){
        return userRepository.findByUserIdentifier(identifier)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private String decrypt(String srt){
        return cipher.decrypt(srt);
    }
}
