package com.shopping.rest.shopping_service.Exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class for handling API-specific errors.
 * This exception extends RuntimeException and adds an HttpStatus to provide more detailed error information.
 */
public class ApiException extends RuntimeException {

    private HttpStatus status;  // HTTP status associated with the exception

    /**
     * Constructor to initialize the ApiException with an HTTP status and a custom message.
     * 
     * @param status - HTTP status indicating the type of error (e.g., BAD_REQUEST, NOT_FOUND)
     * @param message - Custom error message providing details about the exception
     */
    public ApiException(HttpStatus status, String message) {
        super(message);  // Initialize the RuntimeException with the message
        this.status = status;  // Set the HTTP status
    }

    /**
     * Getter for the HTTP status associated with the exception.
     * 
     * @return status - the HttpStatus representing the error type
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Override the toString() method to return a string representation of the exception.
     * This includes the status and the error message.
     * 
     * @return A string representation of the ApiException instance
     */
    @Override
    public String toString() {
        return "ApiException{" +
                "status=" + status +  // Include the status code
                ", message=" + getMessage() +  // Include the custom error message
                '}';
    }
}
