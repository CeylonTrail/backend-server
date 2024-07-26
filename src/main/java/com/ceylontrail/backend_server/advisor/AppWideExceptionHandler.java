package com.ceylontrail.backend_server.advisor;

import com.ceylontrail.backend_server.exception.AlreadyExistingException;
import com.ceylontrail.backend_server.exception.BadCredentialException;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class AppWideExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardResponse> handleNotFoundException(NotFoundException e){
        return new ResponseEntity<>(new StandardResponse(404,"Error",e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistingException.class)
    public ResponseEntity<StandardResponse> handleAlreadyExistingException(AlreadyExistingException e) {
        return new ResponseEntity<>(new StandardResponse(409,"Error",e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialException.class)
    public ResponseEntity<StandardResponse> handleBadCredentialException(BadCredentialException e) {
        return new ResponseEntity<>(new StandardResponse(401,"Error",e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(field, k -> new ArrayList<>()).add(errorMessage);
        });
        return new ResponseEntity<>(new StandardResponse(400, "Validation Errors", errors), HttpStatus.BAD_REQUEST);
    }

}
