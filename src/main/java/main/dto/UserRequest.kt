package main.dto

import jakarta.validation.constraints.AssertFalse
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import org.springframework.validation.annotation.Validated

@Validated
data class UserRequest(
    @field:NotNull
    val name: String,
    @field:NotNull
    @field:Email(message = "Wrong format of email")
    val email: String,
    @field:NotNull
    @field:Size(min=8, message = "Password must be at least 8 characters long")
    val password: String,
    @field:NotNull
    @field:Size(min = 10)
    val phoneNumber: String
)
