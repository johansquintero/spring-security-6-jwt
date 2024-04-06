package com.springsecurity.security.exception;

public class ErrorValidationExceptions extends RuntimeException{
    public ErrorValidationExceptions(String msj){
        super(msj);
    }
}
