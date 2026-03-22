package main.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import main.dto.enums.AdminActionEnum;

@Data
public class AdminRequestActionRequest {
    @NotNull
    @NotBlank
    AdminActionEnum adminAction;
}
