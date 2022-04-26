package com.omarserrar.textme.models.messenger.exceptions;

public class ConversationNotFound extends Throwable {
    private static final String ERROR_MSG = "Conversation Not Found";
    public ConversationNotFound(){
        super(ERROR_MSG);

    }
}
