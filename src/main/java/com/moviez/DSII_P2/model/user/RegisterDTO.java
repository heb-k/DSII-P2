package com.moviez.DSII_P2.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
    @NotBlank @Email(message = "Email inválido") String login, 
    @NotBlank @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String password, 
    UserRole role) {
}
