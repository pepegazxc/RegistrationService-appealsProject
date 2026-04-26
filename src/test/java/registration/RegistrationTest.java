package registration;

import main.dto.enums.RolesEnum;
import main.dto.request.UserRequest;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.event.RegistrationEvent;
import main.repository.UserRepository;
import main.service.application.EmailVerificationService;
import main.service.application.RegistrationService;
import main.service.application.RoleService;
import main.service.infrastructure.CipherService;
import main.service.support.UserIdentifierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationTest {


    @Mock private  UserRepository userRepository;
    @Mock private  CipherService cipher;
    @Mock private  PasswordEncoder encoder;
    @Mock private  UserIdentifierService userIdentifier;
    @Mock private  EmailVerificationService emailService;
    @Mock private  ApplicationEventPublisher publisher;
    @Mock private  RoleService roleService;

    @InjectMocks
    private RegistrationService registration;


    @Test
    public void registerNewUser_successful(){
        UserRequest request = new UserRequest();
        request.setName("UserName");
        request.setSurname("UserSurname");
        request.setEmail("UserEmail@gmail.com");
        request.setPassword("1234567890");
        request.setPhoneNumber("1234567890");
        request.setRole(RolesEnum.user);

        RolesEntity fakeRole = new RolesEntity();
        fakeRole.setRoleName("user");
        when(roleService.findRole(request.getRole().toString()))
                .thenReturn(fakeRole);

        when(cipher.encrypt(anyString())).thenReturn("encryptedStr");

        registration.registration(request);

        verify(userRepository).save(any());
        verify(publisher).publishEvent(any(RegistrationEvent.class));
        verify(emailService).generateTokenForEmail(any());
    }
}
