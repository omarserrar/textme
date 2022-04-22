package com.omarserrar.textme.models.user.exceptions;

public class ActionNotPermitted extends Exception {
    public ActionNotPermitted(){
        super("Action not permitted");
    }
    public ActionNotPermitted(String msg){
        super(msg);
    }
}
