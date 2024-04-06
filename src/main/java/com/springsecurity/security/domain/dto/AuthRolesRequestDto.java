package com.springsecurity.security.domain.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record AuthRolesRequestDto(
        @Size(max = 3, message = "User cannot have more than 3 roles") List<String> rolesListName) {
}
