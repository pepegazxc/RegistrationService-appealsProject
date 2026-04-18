package main.service.application;

import main.dto.enums.RequestsActionEnum;
import main.exception.request.MayorRequestActionNotFoundException;
import main.service.infrastructure.jwt.AuthTokenService;
import org.springframework.stereotype.Service;

@Service
public class MayorRequestResponseResultService {

    private final AuthTokenService jwtService;

    public MayorRequestResponseResultService(AuthTokenService jwtService) {
        this.jwtService = jwtService;
    }

    public String handleMayorRequestResult(RequestsActionEnum action){

        if (action == RequestsActionEnum.APPROVED){
            String jwt = jwtService.generateJwtTokenForCurrentUser();
            return  "Your request has been approved! Your jwt: " + jwt;
        }else if (action == RequestsActionEnum.REJECTED){
            return "Your request has been rejected. You also can try again";
        }

        throw new MayorRequestActionNotFoundException();
    }
}
