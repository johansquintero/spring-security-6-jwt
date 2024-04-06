package com.springsecurity.security.controllers;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.springsecurity.security.exception.ErrorValidationExceptions;
import com.springsecurity.security.exception.Security.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ErrorValidationExceptions.class})
    public ProblemDetail validationException(RuntimeException runtimeException) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, runtimeException.getMessage());
    }
    @ExceptionHandler({UnauthorizedException.class, AuthenticationException.class, JWTVerificationException.class})
    public ProblemDetail unauthorizedException(AuthenticationException authenticationException) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, authenticationException.getMessage());
    }
    @ExceptionHandler({AccessDeniedException.class})
    public ProblemDetail accessDeniedException(AccessDeniedException accessDeniedException) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, accessDeniedException.getMessage());
    }
}
