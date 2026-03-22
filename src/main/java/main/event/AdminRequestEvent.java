package main.event;

import lombok.Getter;

@Getter
public class AdminRequestEvent {

    private final String token;
    private final String email;

    public AdminRequestEvent(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
