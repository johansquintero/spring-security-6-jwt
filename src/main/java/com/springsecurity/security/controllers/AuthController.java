package com.springsecurity.security.controllers;

import com.springsecurity.security.domain.dto.AuthRequestLogInDto;
import com.springsecurity.security.domain.dto.AuthRequestSignUpDto;
import com.springsecurity.security.domain.dto.AuthResponseDto;
import com.springsecurity.security.domain.usecases.IUserUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
@AllArgsConstructor
public class AuthController {
    private final IUserUseCase userService;
    @PostMapping(path = "/log-in")
    public ResponseEntity<AuthResponseDto> logIn(@RequestBody @Valid AuthRequestLogInDto authRequestLogInDto){
        return ResponseEntity.ok(this.userService.logIn(authRequestLogInDto));
    }

    @PostMapping(path = "/sign-up")
    public ResponseEntity<AuthResponseDto> signUp(@RequestBody @Valid AuthRequestSignUpDto authRequestSignUpDto){
        return ResponseEntity.ok(this.userService.signUp(authRequestSignUpDto));
    }
}
