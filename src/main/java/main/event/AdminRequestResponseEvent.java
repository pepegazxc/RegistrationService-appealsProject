package main.event;

import lombok.Getter;
import main.dto.enums.AdminActionEnum;

@Getter
public class AdminRequestResponseEvent {

    private final String email;
    private final AdminActionEnum action;

    public AdminRequestResponseEvent(String email, AdminActionEnum action) {
        this.email = email;
        this.action = action;
    }
}
