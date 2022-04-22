package com.omarserrar.textme.services.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ErrorResponse {
    private final boolean error = true;
    private String message;
    private String errorType = "";

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(Throwable e){
        message = e.getMessage();
        errorType = e.getClass().getTypeName();
    }

}
