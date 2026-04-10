package main.service;

import main.dto.enums.RequestsActionEnum;
import main.exception.request.AdminRequestActionNotFoundException;
import main.service.jwt.AuthTokenService;
import org.springframework.stereotype.Service;

@Service
public class AdminRequestResponseResultService {

    private final AuthTokenService jwtService;

    public AdminRequestResponseResultService(AuthTokenService jwtService) {
        this.jwtService = jwtService;
    }

    public String handleAdminRequestResult(RequestsActionEnum action){

        if (action == RequestsActionEnum.APPROVED){
            String jwt = jwtService.generateJwtTokenForCurrentUser();
            return  "Your request has been approved! Your jwt: " + jwt;
        }else if (action == RequestsActionEnum.REJECTED){
            return "Your request has been rejected. You also can try again";
        }

        throw new AdminRequestActionNotFoundException();
    }
}
