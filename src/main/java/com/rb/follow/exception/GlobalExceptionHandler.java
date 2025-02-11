package com.rb.follow.exception;

import com.rb.follow.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    ResponseEntity<ExceptionResponse> handleUserException(UserException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getLocalizedMessage());
        exceptionResponse.setPath(request.getDescription(false));
        exceptionResponse.setTimeStamp(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FollowException.class)
    ResponseEntity<ExceptionResponse> handleFollowException(FollowException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getLocalizedMessage());
        exceptionResponse.setPath(request.getDescription(false));
        exceptionResponse.setTimeStamp(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
