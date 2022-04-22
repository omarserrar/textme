package com.omarserrar.textme.models.user.exceptions;

public class UserNotFoundException extends Exception{
    private final String message = "User Not Found";
    public UserNotFoundException(){
        super("User Not Found");
    }
}
