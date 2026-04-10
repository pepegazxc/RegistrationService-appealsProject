package main.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import main.dto.enums.RequestsActionEnum;

@Data
public class RequestsActionRequest {
    @NotNull
    RequestsActionEnum action;
}
