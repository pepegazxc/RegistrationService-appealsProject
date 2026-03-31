package main.event;

import lombok.Getter;

@Getter
public class RegistrationEvent {

    private final String email;
    private final String token;

    public RegistrationEvent(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
