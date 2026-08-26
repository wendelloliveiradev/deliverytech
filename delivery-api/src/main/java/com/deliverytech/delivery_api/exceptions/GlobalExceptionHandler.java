package com.deliverytech.delivery_api.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex) {
                Map<String, String> errors = new LinkedHashMap<>();
                for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of(
                                                "message", "Validation failed",
                                                "errors", errors));
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
                Map<String, String> errors = ex.getConstraintViolations().stream()
                                .collect(Collectors.toMap(
                                                violation -> violation.getPropertyPath().toString(),
                                                violation -> violation.getMessage(),
                                                (existing, replacement) -> existing,
                                                LinkedHashMap::new));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of(
                                                "message", "Validation failed",
                                                "errors", errors));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<Map<String, String>> handleBusinessException(BusinessException ex) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(TransactionException.class)
        public ResponseEntity<Map<String, String>> handleTransactionException(TransactionException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", ex.getMessage()));
        }

        @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleJakartaEntityNotFoundException(
                        jakarta.persistence.EntityNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", ex.getMessage()));
        }
}
