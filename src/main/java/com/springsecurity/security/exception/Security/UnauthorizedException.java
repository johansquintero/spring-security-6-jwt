package com.springsecurity.security.exception.Security;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String msj){
        super(msj);
    }
}
