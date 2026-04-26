package admin;

import main.entity.AdminRequestEntity;
import main.entity.AdminRequestStatusEntity;
import main.entity.UsersEntity;
import main.event.AdminRequestEvent;
import main.repository.AdminRequestRepository;
import main.service.application.AdminRequestService;
import main.service.application.AdminRequestStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class AdminRequestServiceTest {

    @Mock private  AdminRequestRepository adminRequestRepository;
    @Mock private  ApplicationEventPublisher publisher;
    @Mock private  AdminRequestStatusService adminRequestStatusService;

    @InjectMocks
    private AdminRequestService adminRequestService;

    @Test
    public void addAdminRequest_successful(){
        UsersEntity fakeUser = new UsersEntity();
        fakeUser.setId(1L);

        AdminRequestStatusEntity fakeStatus = new AdminRequestStatusEntity();
        fakeStatus.setStatus("PENDING");

        when(adminRequestStatusService.findAdminRequestStatus("PENDING"))
                .thenReturn(fakeStatus);

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
}
