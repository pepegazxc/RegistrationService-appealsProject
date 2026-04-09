package main.event;

import lombok.Getter;

@Getter
public class MayorRequestEvent {

    private final String adminEmail;
    private final String token;

    public MayorRequestEvent(String adminEmail, String token) {
        this.adminEmail = adminEmail;
        this.token = token;
    }
}
