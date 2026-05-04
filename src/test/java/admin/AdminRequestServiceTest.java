package admin;

import main.dto.enums.RequestsActionEnum;
import main.dto.request.RequestsActionRequest;
import main.entity.AdminRequestEntity;
import main.entity.AdminRequestStatusEntity;
import main.entity.RolesEntity;
import main.entity.UsersEntity;
import main.event.AdminRequestEvent;
import main.event.RegistrationEvent;
import main.event.RequestResponseEvent;
import main.exception.request.AdminRequestNotFoundException;
import main.exception.request.AdminRequestStatusNotFoundException;
import main.exception.request.RequestExpiredException;
import main.exception.request.RequestIsUsedException;
import main.repository.AdminRequestRepository;
import main.service.application.AdminRequestService;
import main.service.application.AdminRequestStatusService;
import main.service.application.RoleService;
import main.service.infrastructure.CipherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class AdminRequestServiceTest {

    @Mock private  AdminRequestRepository adminRequestRepository;
    @Mock private  ApplicationEventPublisher publisher;
    @Mock private  AdminRequestStatusService adminRequestStatusService;
    @Mock private RoleService roleService;
    @Mock private CipherService cipher;

    @InjectMocks
    private AdminRequestService adminRequestService;

    private UsersEntity fakeUser;
    private AdminRequestStatusEntity fakeStatus;
    private AdminRequestEntity fakeRequest;
    private RequestsActionRequest action;
    private RolesEntity fakeRole;

    @BeforeEach
    void globalSetUp(){
        fakeUser = new UsersEntity();
        fakeStatus = new AdminRequestStatusEntity();
        fakeRequest = new AdminRequestEntity();
        action = new RequestsActionRequest();
        fakeRole = new RolesEntity();
    }

    @Nested
    class SuccessAdd {

        @BeforeEach
        void successSetUp(){
            fakeUser.setId(1L);
            fakeStatus.setStatus("PENDING");
            when(adminRequestStatusService.findAdminRequestStatus(anyString()))
                    .thenReturn(fakeStatus);
        }

        @Test
        public void addAdminRequest_success_shouldAddAdminRequest() {
            fakeUser.setId(1L);
            fakeStatus.setStatus("PENDING");

            adminRequestService.addAdminRequest(fakeUser);

            ArgumentCaptor<AdminRequestEntity> captorRequest = ArgumentCaptor.forClass(AdminRequestEntity.class);
            ArgumentCaptor<AdminRequestEvent> captorEvent = ArgumentCaptor.forClass(AdminRequestEvent.class);

            verify(adminRequestRepository).save(captorRequest.capture());
            verify(publisher).publishEvent(captorEvent.capture());

            AdminRequestEntity fakeAdmin = captorRequest.getValue();
            assertEquals(fakeUser, fakeAdmin.getUser(), "User must be linked to request");
            assertEquals(fakeStatus, fakeAdmin.getStatus(), "Status must be 'PENDING'");
            assertNotNull(fakeAdmin.getToken(), "Token must be generated");

            AdminRequestEvent fakeEvent = captorEvent.getValue();
            assertNotNull(fakeEvent.getToken(), "Token must be transmitted");
            assertEquals(fakeAdmin.getToken(), fakeEvent.getToken(), "Tokens must be equals");
        }

        @Test
        public void handleAdminRequest_success_shouldHandleApprovedAdminRequest() {
            action.setAction(RequestsActionEnum.APPROVED);
            fakeUser.setCipherEmail("encrypted@email");
            fakeRequest.setId(1L);
            fakeRequest.setIsUsed(false);
            fakeRequest.setExpiresAt(LocalDateTime.now().plusDays(1));
            fakeRequest.setToken("token");
            fakeRequest.setUser(fakeUser);
            fakeStatus.setStatus("APPROVED");
            fakeRole.setRoleName("admin");

            when(adminRequestRepository.findByToken("token"))
                    .thenReturn(Optional.of(fakeRequest));
            when(adminRequestStatusService.findAdminRequestStatus("APPROVED"))
                    .thenReturn(fakeStatus);
            when(roleService.findRole("admin"))
                    .thenReturn(fakeRole);
            when(cipher.decrypt("encrypted@email"))
                    .thenReturn("test@mail.com");

            adminRequestService.handleAdminRequest("token", action);

            verify(adminRequestRepository).findByToken("token");
            verify(adminRequestStatusService).findAdminRequestStatus("APPROVED");
            verify(roleService).findRole("admin");

            assertEquals(fakeStatus, fakeRequest.getStatus());
            assertTrue(fakeRequest.getIsUsed());
            assertNotNull(fakeRequest.getReviewedAt());
            assertEquals(fakeRole, fakeUser.getRole());

            ArgumentCaptor<RequestResponseEvent> eventCaptor =
                    ArgumentCaptor.forClass(RequestResponseEvent.class);

            verify(publisher).publishEvent(eventCaptor.capture());

            RequestResponseEvent event = eventCaptor.getValue();

            assertEquals("test@mail.com", event.getEmail());
            assertEquals(RequestsActionEnum.APPROVED, event.getAction());
        }
        @Test
        public void handleAdminRequest_success_shouldHandleRejectedAdminRequest() {
            action.setAction(RequestsActionEnum.REJECTED);
            fakeUser.setCipherEmail("encrypted@email");
            fakeRequest.setId(1L);
            fakeRequest.setIsUsed(false);
            fakeRequest.setExpiresAt(LocalDateTime.now().plusDays(1));
            fakeRequest.setToken("token");
            fakeRequest.setUser(fakeUser);
            fakeStatus.setStatus("REJECTED");
            fakeRole.setRoleName("user");

            when(adminRequestRepository.findByToken("token"))
                    .thenReturn(Optional.of(fakeRequest));
            when(adminRequestStatusService.findAdminRequestStatus("REJECTED"))
                    .thenReturn(fakeStatus);
            when(roleService.findRole("user"))
                    .thenReturn(fakeRole);
            when(cipher.decrypt("encrypted@email"))
                    .thenReturn("test@mail.com");

            adminRequestService.handleAdminRequest("token", action);

            verify(adminRequestRepository).findByToken("token");
            verify(adminRequestStatusService).findAdminRequestStatus("REJECTED");
            verify(roleService).findRole("user");

            assertEquals(fakeStatus, fakeRequest.getStatus());
            assertTrue(fakeRequest.getIsUsed());
            assertNotNull(fakeRequest.getReviewedAt());
            assertEquals(fakeRole, fakeUser.getRole());

            ArgumentCaptor<RequestResponseEvent> eventCaptor =
                    ArgumentCaptor.forClass(RequestResponseEvent.class);

            verify(publisher).publishEvent(eventCaptor.capture());

            RequestResponseEvent event = eventCaptor.getValue();

            assertEquals("test@mail.com", event.getEmail());
            assertEquals(RequestsActionEnum.REJECTED, event.getAction());
        }
    }

    @Nested
    class FailedAdd {
        @Test
        public void addAdminRequest_failed_shouldReturnStatusNotFoundException(){
            fakeUser.setId(1L);
            fakeStatus.setStatus("fake-status");
            fakeRequest.setId(1L);
            fakeRequest.setIsUsed(false);
            fakeRequest.setExpiresAt(LocalDateTime.now().plusDays(999));
            fakeRequest.setUser(fakeUser);

            when(adminRequestStatusService.findAdminRequestStatus(any()))
                    .thenThrow(new AdminRequestStatusNotFoundException());

            assertThrows(AdminRequestStatusNotFoundException.class,
                    () -> adminRequestService.addAdminRequest(fakeUser));
        }
    }
    @Test
    public void handleAdminRequest_failed_shouldReturnRequestIsUsedException(){
        action.setAction(RequestsActionEnum.APPROVED);
        fakeUser.setId(1L);
        fakeStatus.setStatus("fake-status");
        fakeRequest.setId(1L);
        fakeRequest.setIsUsed(true);
        fakeRequest.setExpiresAt(LocalDateTime.now().plusDays(999));
        fakeRequest.setUser(fakeUser);

        when(adminRequestRepository.findByToken("token"))
                .thenReturn(Optional.of(fakeRequest));

        assertThrows(RequestIsUsedException.class,
                () -> adminRequestService.handleAdminRequest("token",action ));
    }

    @Test
    public void handleAdminRequest_failed_shouldReturnRequestNotFoundException(){
        action.setAction(RequestsActionEnum.APPROVED);
        fakeUser.setId(1L);
        fakeStatus.setStatus("fake-status");
        fakeRequest.setId(1L);
        fakeRequest.setIsUsed(true);
        fakeRequest.setExpiresAt(LocalDateTime.now().plusDays(999));
        fakeRequest.setUser(fakeUser);

        when(adminRequestRepository.findByToken("token"))
                .thenThrow(new AdminRequestNotFoundException());

        assertThrows(AdminRequestNotFoundException.class,
                () -> adminRequestService.handleAdminRequest("token", action));
    }
    @Test
    public void handleAdminRequest_failed_shouldReturnTokenIsExpired(){
        action.setAction(RequestsActionEnum.APPROVED);
        fakeUser.setId(1L);
        fakeStatus.setStatus("fake-status");
        fakeRequest.setId(1L);
        fakeRequest.setIsUsed(false);
        fakeRequest.setExpiresAt(LocalDateTime.now().minusDays(1));
        fakeRequest.setUser(fakeUser);

        when(adminRequestRepository.findByToken("token"))
                .thenReturn(Optional.of(fakeRequest));

        assertThrows(RequestExpiredException.class,
                () -> adminRequestService.handleAdminRequest("token", action));
    }
}
