package com.omarserrar.textme.models.messenger.exceptions;

import com.omarserrar.textme.services.responses.ErrorResponse;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConversationNotFound extends Throwable {
    private static final String ERROR_MSG = "Conversation Not Found";
    public ConversationNotFound(){
        super(ERROR_MSG);

    }
}
