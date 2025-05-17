package com.eb2.markdownnotetakingapp.exception;

import com.eb2.markdownnotetakingapp.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(InvalidFile.class)
    public ResponseEntity<Error> handleInvalidFile(InvalidFile invalidFile) {

        return ResponseEntity.status(500)
                .body(new Error(invalidFile.getMessage(), 500, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Error> handleEmptyFileException(EmptyFileException emptyFileException) {

        return ResponseEntity.status(400)
                .body(new Error(emptyFileException.getMessage(), 500, HttpStatus.BAD_REQUEST));
    }


}
