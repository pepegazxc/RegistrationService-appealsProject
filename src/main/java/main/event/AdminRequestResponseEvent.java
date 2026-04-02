package main.event;

import lombok.Getter;

@Getter
public class AdminRequestResponseEvent {

    private final String email;

    public AdminRequestResponseEvent(String email) {
        this.email = email;
    }
}
