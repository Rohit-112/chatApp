package com.demo.chatApp.exception;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message){
        super(message);
    }
}
