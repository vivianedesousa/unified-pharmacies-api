package com.farmacies.unifiedpharmacies.exception.global;

import com.farmacies.unifiedpharmacies.dto.errors.ErrorResponseDTO;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.exception.validation.BusinessValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request) {
        String path = request.getRequestURI();
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                404,
                "Not Found",
                exception.getMessage(),
                path,
                null
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseDTO);
    }


    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(
            ResourceConflictException exception,
            HttpServletRequest request) {
        String path = request.getRequestURI();
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                409,
                "Conflict",
                exception.getMessage(),
                path,
                null
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDTO);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessValidation(
            BusinessValidationException validationException,
            HttpServletRequest request) {
        String path = request.getRequestURI();
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                400,
                "Bad Request",
                validationException.getMessage(),
                path,
                null

        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDTO);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {

                    fieldErrors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    );
                });

        String path = request.getRequestURI();
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                400,
                "Bad Request",
                "Validation failed.",
                path,
                fieldErrors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDTO);
    }
}