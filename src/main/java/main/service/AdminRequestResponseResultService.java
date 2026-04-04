package main.service;

import main.dto.enums.AdminActionEnum;
import main.exception.request.AdminRequestStatusNotFoundException;
import main.service.jwt.AuthTokenService;
import org.springframework.stereotype.Service;

@Service
public class AdminRequestResponseResultService {

    private final AuthTokenService jwtService;

    public AdminRequestResponseResultService(AuthTokenService jwtService) {
        this.jwtService = jwtService;
    }

    public String handleAdminRequestResult(AdminActionEnum action){

        if (action == AdminActionEnum.APPROVED){
            String jwt = jwtService.generateJwtTokenForCurrentUser();
            return  "Your request has been approved! Your jwt: " + jwt;
        }else if (action == AdminActionEnum.REJECTED){
            return "Your request has been rejected. You also can try again";
        }

        throw new AdminRequestStatusNotFoundException();
    }
}
