package com.ceylontrail.backend_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadyExistingException extends RuntimeException {
    public AlreadyExistingException(String message) {
        super(message);
    }
}
