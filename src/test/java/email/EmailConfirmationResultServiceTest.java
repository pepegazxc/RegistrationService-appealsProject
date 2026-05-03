package email;

import main.dto.response.EmailConfirmResultResponse;
import main.dto.response.EmailConfirmationResponse;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.service.application.AdminRequestService;
import main.service.application.EmailConfirmationResultService;
import main.service.application.EmailVerificationService;
import main.service.application.MayorRequestService;
import main.service.infrastructure.jwt.AuthTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailConfirmationResultServiceTest {

    @Mock private AuthTokenService jwt;
    @Mock private EmailVerificationService email;
    @Mock private AdminRequestService adminRequestService;
    @Mock private MayorRequestService mayorRequestService;

    @InjectMocks
    private EmailConfirmationResultService service;

    @Test
    public void confirmationResult_success_shouldReturnJwt(){
        String fakeToken = "token";
        RolesEntity fakeRole = new RolesEntity();
        fakeRole.setRoleName("USER");
        UsersEntity fakeUser = new UsersEntity();
        fakeUser.setRole(fakeRole);

        when(email.confirmUserEmail(fakeToken))
                .thenReturn(fakeUser);

        when(jwt.generateJwtTokenForCurrentUser())
                .thenReturn("jwt");


        EmailConfirmResultResponse email = service.confirmationResult(fakeToken);

        verify(jwt).generateJwtTokenForCurrentUser();
        assertEquals("jwt", email.getToken());
        verifyNoInteractions(adminRequestService, mayorRequestService);


    }

}
