package main.controller;

import jakarta.validation.Valid;
import main.dto.request.RequestsActionRequest;
import main.dto.response.ConfirmRequestsResponse;
import main.service.application.AdminRequestService;
import main.service.application.MayorRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RequestController {

    private final MayorRequestService mayorRequestService;
    private final AdminRequestService adminRequestService;

    public RequestController(MayorRequestService mayorRequestService, AdminRequestService adminRequestService) {
        this.mayorRequestService = mayorRequestService;
        this.adminRequestService = adminRequestService;
    }

    @PatchMapping("/admin/request")
    public ResponseEntity<ConfirmRequestsResponse> confirmAdminRequest(@RequestParam String token, @RequestBody @Valid RequestsActionRequest adminAction){
        adminRequestService.handleAdminRequest(token, adminAction);

        return ResponseEntity.ok().body(
                new ConfirmRequestsResponse(
                        "Request information has successfully changed"
                )
        );
    }

    @PatchMapping("/mayor/request")
    public ResponseEntity<ConfirmRequestsResponse> confirmMayorRequest(@RequestParam String token, @RequestBody @Valid RequestsActionRequest mayorAction){
        mayorRequestService.handleMayorRequest(token, mayorAction);

        return ResponseEntity.ok().body(
                new ConfirmRequestsResponse(
                        "Request information has successfully changed"
                )
        );
    }
}
