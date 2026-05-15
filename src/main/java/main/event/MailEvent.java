package main.event;

import lombok.Data;

@Data
public abstract class MailEvent {
    private String email;
    private String token;

    public void setM() {
    }
}
