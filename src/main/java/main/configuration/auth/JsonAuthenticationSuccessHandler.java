package main.configuration.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.dto.response.SuccessLoginResponse;
import main.service.infrastructure.jwt.AuthTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthTokenService jwt;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonAuthenticationSuccessHandler(AuthTokenService jwt) {
        this.jwt = jwt;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        SuccessLoginResponse loginResponse = new SuccessLoginResponse(
                "Success login",
                jwt.generateJwtTokenForCurrentUser()
        );

        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
    }
}
