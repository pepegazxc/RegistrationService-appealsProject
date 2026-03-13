package main.event;

import lombok.Getter;

public class UserRegisteredEvent {

    private final String email;
    private final String token;

    public UserRegisteredEvent(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail(){
        return email;
    }
    public String getToken(){
        return token;
    }
}
