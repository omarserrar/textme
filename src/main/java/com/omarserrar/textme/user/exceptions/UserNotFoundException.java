package com.omarserrar.textme.user.exceptions;

public class UserNotFoundException extends Throwable{
    public UserNotFoundException(){
        super("User Not Found");
    }
}
