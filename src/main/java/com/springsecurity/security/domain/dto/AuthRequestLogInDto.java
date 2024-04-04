package com.springsecurity.security.domain.dto;

import lombok.NonNull;

public record AuthRequestLogInDto(@NonNull String username, @NonNull String password){
}
