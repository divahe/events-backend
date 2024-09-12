package com.example.events.exceptions;

import com.example.events.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({FailedAuthenticationException.class})
    public ResponseEntity<ErrorResponseDto> handleFailedAuthentication(FailedAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler({EventNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleEventNotFoundException(EventNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<ErrorResponseDto> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponseDto(ex.getMessage()));
    }
}
