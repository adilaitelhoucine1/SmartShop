package org.smartshop.smartshop.exception;

import org.smartshop.smartshop.DTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            String ex, WebRequest request) {
        String message = ex + "Not Found";
        ErrorResponseDTO error = new ErrorResponseDTO(400, "Validation Error", message, request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler (UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO(401, "Unauthorized", ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResource(DuplicateResourceException ex
    , WebRequest request){

        ErrorResponseDTO errorResponseDTO= new ErrorResponseDTO(409,"Duplicate",ex.getMessage(),request.getDescription(false));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

}