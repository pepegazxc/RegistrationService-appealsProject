package main.event;

import lombok.Getter;

@Getter
public class UserRegisteredEvent {

    private final String email;
    private final String token;

    public UserRegisteredEvent(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
