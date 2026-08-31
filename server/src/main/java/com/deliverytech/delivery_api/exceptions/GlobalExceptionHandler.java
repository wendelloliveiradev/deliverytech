package com.deliverytech.delivery_api.exceptions;

import java.util.LinkedHashMap;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex, HttpServletRequest request) {
                Map<String, String> errors = new LinkedHashMap<>();
                for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }

                return response(HttpStatus.BAD_REQUEST, "Validation failed", errors, request);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex,
                        HttpServletRequest request) {
                Map<String, String> errors = ex.getConstraintViolations().stream()
                                .collect(Collectors.toMap(
                                                violation -> violation.getPropertyPath().toString(),
                                                violation -> violation.getMessage(),
                                                (existing, replacement) -> existing,
                                                LinkedHashMap::new));

                return response(HttpStatus.BAD_REQUEST, "Validation failed", errors, request);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex,
                        HttpServletRequest request) {
                return response(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.emptyMap(), request);
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ApiError> handleValidationException(ValidationException ex, HttpServletRequest request) {
                return response(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.emptyMap(), request);
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiError> handleBusinessException(BusinessException ex, HttpServletRequest request) {
                return response(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), Collections.emptyMap(), request);
        }

        @ExceptionHandler(TransactionException.class)
        public ResponseEntity<ApiError> handleTransactionException(TransactionException ex,
                        HttpServletRequest request) {
                return response(HttpStatus.CONFLICT, ex.getMessage(), Collections.emptyMap(), request);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex,
                        HttpServletRequest request) {
                return response(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.emptyMap(), request);
        }

        @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
        public ResponseEntity<ApiError> handleJakartaEntityNotFoundException(
                        jakarta.persistence.EntityNotFoundException ex, HttpServletRequest request) {
                return response(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.emptyMap(), request);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                        HttpServletRequest request) {
                return response(HttpStatus.BAD_REQUEST, "Malformed request body", Collections.emptyMap(), request);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex,
                        HttpServletRequest request) {
                return response(HttpStatus.UNAUTHORIZED, "Invalid email or password.", Collections.emptyMap(), request);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleUnexpectedException(Exception ex, HttpServletRequest request) {
                return response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",
                                Collections.emptyMap(),
                                request);
        }

        private ResponseEntity<ApiError> response(HttpStatus status, String message, Map<String, String> details,
                        HttpServletRequest request) {
                return ResponseEntity.status(status)
                                .body(new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message,
                                                request.getRequestURI(), details));
        }
}
