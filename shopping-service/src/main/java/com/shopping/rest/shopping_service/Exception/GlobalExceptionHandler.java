package com.shopping.rest.shopping_service.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.shopping.rest.shopping_service.Paylods.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler class that handles different types of exceptions
 * thrown by the application. It provides customized responses for each type
 * of exception.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ApiException thrown in the application.
     * 
     * @param ex The ApiException instance.
     * @return ResponseEntity containing an ApiResponse with the error message and status.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {

        // Extract the message and status from the exception
        String message = ex.getMessage();
        HttpStatus status = ex.getStatus();

        // Create an ApiResponse to send as the response body
        ApiResponse apiResponse = new ApiResponse(message, false);

        // Return the response with the appropriate status
        return new ResponseEntity<>(apiResponse, status);
    }

    /**
     * Handles BadRequestException thrown in the application.
     * 
     * @param ex The BadRequestException instance.
     * @return ResponseEntity containing an ApiResponse with the error message.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> BadRequestException(BadRequestException ex) {
        // Extract the error message
        String message = ex.getMessage();

        // Create the ApiResponse with the error message
        ApiResponse apiResponse = new ApiResponse(message, false);

        // Return a BAD_REQUEST status response
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ConstraintViolationException for invalid input validation.
     * 
     * @param ex The ConstraintViolationException instance.
     * @return ResponseEntity containing a map of field names and their validation error messages.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> ConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> response = new HashMap<>();
        
        // Process each violation and store the field name and error message in the response map
        ex.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String message = error.getMessageTemplate();
            response.put(fieldName, message);
        });

        // Return a BAD_REQUEST status response with the violation details
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException for invalid method argument bindings.
     * 
     * @param ex The MethodArgumentNotValidException instance.
     * @return ResponseEntity containing a map of field names and their corresponding error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        
        // Process each field error and store the field name and error message in the response map
        ex.getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            response.put(fieldName, message);
        });
        
        // Return a BAD_REQUEST status response with the field error details
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

}
