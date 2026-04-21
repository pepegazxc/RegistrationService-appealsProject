package main.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LoginEvent {

    private final String email;
    private final LocalDateTime time;

    public LoginEvent(String email, LocalDateTime time) {
        this.email = email;
        this.time = time;
    }
}
