package main.dto.response;

public class EmailConfirmResultResponse {

    private final String message;
    private final String token;

    public EmailConfirmResultResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    private static EmailConfrimationResult userEmailConfirm(String token){
        return new EmailConfirmationResult(
                "Your email has been successfully confirmed",
                token
        );
    }

    private static EmailConfrimationResult adminEmailConfirm(){
        return new EmailConfrimationResult(
                "Your email has been successfully confirmed. Your request has been added",
                "You will get your token after admin confirmation"
        );
    }
}
