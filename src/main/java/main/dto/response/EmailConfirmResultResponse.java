package main.dto.response;

import lombok.Getter;

@Getter
public class EmailConfirmResultResponse {
    private final String message;
    private final String token;

    private EmailConfirmResultResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public static EmailConfirmResultResponse userEmailConfirmed(String token) {
        return new EmailConfirmResultResponse(
                "Your email has been successfully confirmed",
                token
        );
    }

    public static EmailConfirmResultResponse adminEmailConfirmed() {
        return new EmailConfirmResultResponse(
                "Your email has been successfully confirmed. Your request has been added",
                "You will get your token after admin confirmation"
        );
    }
}
