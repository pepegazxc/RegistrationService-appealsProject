package main.service.application;

import main.dto.enums.RequestsActionEnum;
import main.exception.request.RequestActionNotFoundException;
import main.service.infrastructure.jwt.AuthTokenService;
import org.springframework.stereotype.Service;

@Service
public class RequestResponseResultService {

    private final AuthTokenService jwtService;

    public RequestResponseResultService(AuthTokenService jwtService) {
        this.jwtService = jwtService;
    }

    public String handleRequestResult(RequestsActionEnum action){

        if (action == RequestsActionEnum.APPROVED){
            String jwt = jwtService.generateJwtTokenForCurrentUser();
            return  "Your request has been approved! Your jwt: " + jwt;
        }else if (action == RequestsActionEnum.REJECTED){
            return "Your request has been rejected. You also can try again";
        }

        throw new RequestActionNotFoundException();
    }
}
