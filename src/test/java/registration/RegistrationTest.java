package registration;

import main.dto.enums.RolesEnum;
import main.dto.request.UserRequest;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.exception.user.RoleNotFoundException;
import main.exception.user.UserIdentifierException;
import main.producer.KafkaProducer;
import main.repository.UserRepository;
import main.service.application.EmailVerificationService;
import main.service.application.RegistrationService;
import main.service.application.RoleService;
import main.service.infrastructure.CipherService;
import main.service.support.UserIdentifierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationTest {

    @Mock private UserRepository userRepository;
    @Mock private CipherService cipher;
    @Mock private PasswordEncoder encoder;
    @Mock private UserIdentifierService userIdentifier;
    @Mock private EmailVerificationService emailService;
    @Mock private KafkaProducer kafka;
    @Mock private RoleService roleService;

    @InjectMocks
    private RegistrationService registration;

    private UserRequest request;
    private RolesEntity fakeRole;

    @BeforeEach
    void globalSetUp() {
        request = new UserRequest();
        request.setName("UserName");
        request.setSurname("UserSurname");
        request.setEmail("UserEmail@gmail.com");
        request.setPassword("1234567890");
        request.setPhoneNumber("1234567890");

        fakeRole = new RolesEntity();
    }

    @Nested
    class SuccessRegistration {

        @BeforeEach
        void successSetUp() {
            when(emailService.generateTokenForEmail(any(UsersEntity.class)))
                    .thenReturn("token");
            when(cipher.decrypt(any())).thenReturn("UserEmail@gmail.com");
            when(cipher.encrypt(any())).thenReturn("encrypted");
            when(userIdentifier.generate()).thenReturn("USR-111");
            when(encoder.encode(any())).thenReturn("hash");
        }

        @Test
        public void registration_success_shouldRegisterUserWithUserRole() {
            request.setRole(RolesEnum.user);
            fakeRole.setRoleName("user");

            when(roleService.findRole(request.getRole().toString()))
                    .thenReturn(fakeRole);

            registration.registration(request);

            ArgumentCaptor<UsersEntity> captorUser = ArgumentCaptor.forClass(UsersEntity.class);
            verify(userRepository).save(captorUser.capture());

            UsersEntity savedUser = captorUser.getValue();
            assertEquals(fakeRole, savedUser.getRole(), "Roles must be equals");

            verify(emailService).generateTokenForEmail(savedUser);
            verify(kafka).handleRegistration("UserEmail@gmail.com", "token");
        }

        @Test
        public void registration_success_shouldRegisterNewUserWithPendingAdminRole() {
            request.setRole(RolesEnum.admin);
            fakeRole.setRoleName("PENDING_ADMIN");

            when(roleService.findRole("PENDING_ADMIN"))
                    .thenReturn(fakeRole);

            registration.registration(request);

            ArgumentCaptor<UsersEntity> captorUser = ArgumentCaptor.forClass(UsersEntity.class);
            verify(roleService).findRole("PENDING_ADMIN");
            verify(userRepository).save(captorUser.capture());
            verify(userRepository).save(argThat(user ->
                    user.getRole().getRoleName().equals("PENDING_ADMIN")));

            UsersEntity savedUser = captorUser.getValue();
            assertEquals(fakeRole, savedUser.getRole(), "Roles must be equals");

            verify(emailService).generateTokenForEmail(savedUser);
            verify(kafka).handleRegistration("UserEmail@gmail.com", "token");
        }

        @Test
        public void registration_success_shouldRegisterNewUserWithPendingMayorRole() {
            request.setRole(RolesEnum.mayor);
            fakeRole.setRoleName("PENDING_MAYOR");

            when(roleService.findRole("PENDING_MAYOR"))
                    .thenReturn(fakeRole);

            registration.registration(request);

            ArgumentCaptor<UsersEntity> captorUser = ArgumentCaptor.forClass(UsersEntity.class);
            verify(roleService).findRole("PENDING_MAYOR");
            verify(userRepository).save(captorUser.capture());
            verify(userRepository).save(argThat(user ->
                    user.getRole().getRoleName().equals("PENDING_MAYOR")));

            UsersEntity savedUser = captorUser.getValue();
            assertEquals(fakeRole, savedUser.getRole(), "Roles must be equals");

            verify(emailService).generateTokenForEmail(savedUser);
            verify(kafka).handleRegistration("UserEmail@gmail.com", "token");
        }
    }

    @Nested
    class FailedRegistration {

        @BeforeEach
        void failedSetUp() {
            request.setRole(RolesEnum.user);
        }

        @Test
        public void registration_failed_shouldReturnRoleNotFoundException() {
            when(roleService.findRole(anyString())).thenThrow(new RoleNotFoundException());

            assertThrows(RoleNotFoundException.class, () ->
                    registration.registration(request));

            verify(userRepository, never()).save(any());
            verifyNoInteractions(kafka);
        }

        @Test
        public void registration_failed_shouldReturnUserIdentifierException() {
            when(userIdentifier.generate()).thenThrow(new UserIdentifierException());

            assertThrows(UserIdentifierException.class, () ->
                    registration.registration(request));

            verify(userRepository, never()).save(any());
            verifyNoInteractions(kafka);
        }

        @Test
        public void registration_failed_tooManyIdentifierAttempts() {
            when(userRepository.existsByUserIdentifier(anyString())).thenReturn(true);
            when(userIdentifier.generate()).thenReturn("USER");

            assertThrows(UserIdentifierException.class, () ->
                    registration.registration(request));

            verify(userIdentifier, atLeast(10)).generate();
            verify(userRepository, never()).save(any());
            verifyNoInteractions(kafka);
        }
    }
}