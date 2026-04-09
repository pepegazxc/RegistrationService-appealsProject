package main.event;

import lombok.Getter;

@Getter
public class AdminRequestEvent {

    private final String token;

    public AdminRequestEvent(String token) {
        this.token = token;
    }
}
