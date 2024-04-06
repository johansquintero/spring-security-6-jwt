package com.springsecurity.security.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record AuthRequestSignUpDto(
        @NotBlank String username,
        @NotBlank String password,
        @NotNull AuthRolesRequestDto authRolesRequestDto) {
}
