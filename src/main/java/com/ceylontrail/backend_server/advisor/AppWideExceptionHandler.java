package com.ceylontrail.backend_server.advisor;

import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity handleNotFoundException(ChangeSetPersister.NotFoundException e){
        return new ResponseEntity(new StandardResponse(404,"Error",e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
