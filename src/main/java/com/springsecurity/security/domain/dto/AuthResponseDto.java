package com.springsecurity.security.domain.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record AuthResponseDto(String username, String message, String jwt, boolean status) {
}
