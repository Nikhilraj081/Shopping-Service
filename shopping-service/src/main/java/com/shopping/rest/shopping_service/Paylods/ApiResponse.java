package com.shopping.rest.shopping_service.Paylods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponse class is used for standardizing the API response structure.
 * It contains a message and a status indicating the success or failure of an operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    /**
     * The message that provides information about the success or failure of an operation.
     */
    private String message;

    /**
     * A boolean status indicating whether the operation was successful (true) or failed (false).
     */
    private boolean status;
}
