package org.smartshop.smartshop.exception;

import org.smartshop.smartshop.DTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.stream.Collectors;
import org.springframework.web.bind.MethodArgumentNotValidException;



@RestControllerAdvice
public class GlobalExceptionHandler {


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
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex
    , WebRequest request){

        ErrorResponseDTO errorResponseDTO= new ErrorResponseDTO(409,"Not Found",ex.getMessage(),request.getDescription(false));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }
    @ExceptionHandler(SessionEmtyException.class)
    public ResponseEntity<ErrorResponseDTO> handleSessionEmtyException(SessionEmtyException ex
    , WebRequest request){

        ErrorResponseDTO errorResponseDTO= new ErrorResponseDTO(401,"No User Connect",ex.getMessage(),request.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDTO);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {


        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                400, "Validation Error", message, request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

}