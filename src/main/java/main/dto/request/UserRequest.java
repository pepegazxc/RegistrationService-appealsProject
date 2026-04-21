package main.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import main.dto.enums.RolesEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class UserRequest{
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String surname;

    @NotNull
    @NotBlank
    @Email(message = "Wrong format of email")
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 symbols")
    private String password;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d{7,18}$", message = "Your phone number must contains 7-18 digits")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    private RolesEnum role;
}