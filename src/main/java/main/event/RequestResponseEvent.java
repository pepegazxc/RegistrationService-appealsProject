package main.event;

import lombok.Getter;
import main.dto.enums.RequestsActionEnum;

@Getter
public class RequestResponseEvent {

    private final String email;
    private final RequestsActionEnum action;

    public RequestResponseEvent(String email, RequestsActionEnum action) {
        this.email = email;
        this.action = action;
    }
}
