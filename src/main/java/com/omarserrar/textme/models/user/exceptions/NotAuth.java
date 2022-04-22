package com.omarserrar.textme.models.user.exceptions;

public class NotAuth extends Exception{
    public NotAuth(){
        super("Not authenticated");
    }
}
