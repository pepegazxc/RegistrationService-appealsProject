package main.advice.mapper;

import main.exception.AppException;
import main.exception.request.RequestNotAllowedException;
import main.exception.user.EmailAlreadyExistsException;
import main.exception.user.PhoneNumberAlreadyExistsException;
import main.exception.user.RegistrationFailedException;
import main.exception.user.UserIdentifierException;
import org.springframework.stereotype.Component;

@Component
public class ConstraintViolationMapper {

    public AppException map(String constraintName){
        return switch (constraintName) {
            case "users_cipher_email_key" ->
                new EmailAlreadyExistsException();
            case "users_cipher_phone_number_key" ->
                new PhoneNumberAlreadyExistsException();
            case "users_identifier_key" ->
                new UserIdentifierException();
            case "one_admin_request_per_user" ->
                new RequestNotAllowedException();
            default ->
                    new RegistrationFailedException();
        };
    }
}
