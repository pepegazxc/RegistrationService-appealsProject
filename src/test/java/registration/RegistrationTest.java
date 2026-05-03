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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationTest {


    @Mock private  UserRepository userRepository;
    @Mock private  CipherService cipher;
    @Mock private PasswordEncoder encoder;
    @Mock private  UserIdentifierService userIdentifier;
    @Mock private  EmailVerificationService emailService;
    @Mock private  ApplicationEventPublisher publisher;
    @Mock private  RoleService roleService;

    @InjectMocks
    private RegistrationService registration;


    @Test
    public void registration_success_shouldRegisterUserWithUserRole(){
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

        when(emailService.generateTokenForEmail(any(UsersEntity.class)))
                .thenReturn("token");

        when(cipher.decrypt(any())).thenReturn("UserEmail@gmail.com");

        when(userIdentifier.generate()).thenReturn("USR-111");

        when(encoder.encode((any()))).thenReturn("hash");


        registration.registration(request);

        ArgumentCaptor<UsersEntity> captorUser = ArgumentCaptor.forClass(UsersEntity.class);
        ArgumentCaptor<RegistrationEvent> captorEvent = ArgumentCaptor.forClass(RegistrationEvent.class);

        verify(userRepository).save(captorUser.capture());

        UsersEntity fakeUser = captorUser.getValue();
        assertEquals(fakeRole, fakeUser.getRole(), "Roles must be equals");

        verify(publisher).publishEvent(captorEvent.capture());

        RegistrationEvent fakeEvent = captorEvent.getValue();
        assertNotNull(fakeEvent.getEmail(), "Email must not be empty");
        assertNotNull(fakeEvent.getToken(), "Token for email confirm must not be empty");
        assertEquals("token", fakeEvent.getToken(), "Token should match generated one");

        verify(emailService).generateTokenForEmail(fakeUser);

    }
}
