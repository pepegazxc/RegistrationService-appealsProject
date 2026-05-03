package mayor;

import main.entity.MayorRequestEntity;
import main.entity.MayorRequestStatusEntity;
import main.entity.UsersEntity;
import main.repository.MayorRequestRepository;
import main.service.application.AdminsEmailsService;
import main.service.application.MayorRequestService;
import main.service.application.MayorRequestStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MayorRequestServiceTest {

    @Mock private MayorRequestStatusService requestStatusService;
    @Mock private MayorRequestRepository mayorRequestRepository;
    @Mock private AdminsEmailsService adminsEmailsService;

    @InjectMocks
    private MayorRequestService mayorRequestService;

    @Test
    public void addMayorRequest_successful_shouldAddMayorRequest(){
        UsersEntity fakeUser = new UsersEntity();
        fakeUser.setId(1L);

        MayorRequestStatusEntity fakeStatus = new MayorRequestStatusEntity();
        fakeStatus.setStatus("PENDING");

        MayorRequestEntity fakeMayor = new MayorRequestEntity();
        fakeMayor.setStatus(fakeStatus);
        fakeMayor.setToken("token");

        when(requestStatusService.findMayorRequestStatus("PENDING"))
                .thenReturn(fakeStatus);

        when(mayorRequestRepository.save(any(MayorRequestEntity.class)))
                .thenReturn(fakeMayor);


        mayorRequestService.addMayorRequest(fakeUser);

        ArgumentCaptor<MayorRequestEntity> captorRequest = ArgumentCaptor.forClass(MayorRequestEntity.class);

        verify(mayorRequestRepository).save(captorRequest.capture());
        MayorRequestEntity savedToRequest = captorRequest.getValue();

        assertEquals(fakeUser, savedToRequest.getUser(), "User must be matches");
        assertEquals("PENDING", savedToRequest.getStatus().getStatus(), "Status must be matches");

        verify(adminsEmailsService).sendMailToAdmins(savedToRequest.getToken());
    }
}
