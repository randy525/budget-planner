package org.usm.budgetplanner.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.usm.budgetplanner.dto.response.ErrorResponse;
import org.usm.budgetplanner.exception.ApplicationException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation error: ", e);
        List<ErrorResponse> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) ->
                errors.add(new ErrorResponse(error.getDefaultMessage())));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException e) {
        log.error("Application error: ", e);
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Error: ", e);
        ErrorResponse response = ErrorResponse.builder()
                .message("Something went wrong")
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


}
