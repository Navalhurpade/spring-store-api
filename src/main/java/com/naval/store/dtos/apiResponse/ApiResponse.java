package com.naval.store.dtos.apiResponse;

import java.time.LocalDateTime;

public record ApiResponse<T>(boolean success, String message, T data, Object errors, String timestamp) {
    public ApiResponse {
        if (message == null) {
            message = success ? "Operation successful" : "Operation failed";
        }
    }

    public ApiResponse(boolean success, String message, T data, Object errors) {
        this(success, message, data, errors, LocalDateTime.now().toString());
    }

    public ApiResponse(boolean success, String message, T data) {
        this(success, message, data, null, LocalDateTime.now().toString());
    }
}
