package main.event;

import lombok.Getter;
import main.dto.enums.RequestsActionEnum;

@Getter
public class AdminRequestResponseEvent {

    private final String email;
    private final RequestsActionEnum action;

    public AdminRequestResponseEvent(String email, RequestsActionEnum action) {
        this.email = email;
        this.action = action;
    }
}
