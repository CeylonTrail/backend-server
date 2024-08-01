package com.ceylontrail.backend_server.advisor;

import com.ceylontrail.backend_server.exception.*;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        return new ResponseEntity<>(new StandardResponse(400, "Validation Error", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardResponse> handleTypeMismatchExceptions(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        String fieldName = ex.getName();
        String errorMessage = "Invalid value for " + fieldName + ": " + ex.getValue();
        errors.put(fieldName, errorMessage);
        return new ResponseEntity<>(new StandardResponse(400, "Type Mismatch Error", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardResponse> handleUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(new StandardResponse(401,"Error",e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardResponse> handleConflictException(ConflictException e) {
        return new ResponseEntity<>(new StandardResponse(409,"Error",e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardResponse> handelBadRequestException(ConflictException e) {
        return new ResponseEntity<>(new StandardResponse(400,"Error",e.getMessage()), HttpStatus.BAD_REQUEST);
    }



}
