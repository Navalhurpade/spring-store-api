package com.codewithmosh.store.utils;

import com.codewithmosh.store.dtos.apiResponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class ResponseUtils {
    public <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        var response = new ApiResponse<>(true, "Operation successful", data);
        return ResponseEntity.ok(response);
    }

    public <T> ResponseEntity<ApiResponse<T>> ok(T data, HttpStatus status, URI uri) {
        var response = new ApiResponse<>(true, "Operation successful", data);
        if (uri != null) return ResponseEntity.created(uri).body(response);
        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<ApiResponse<Void>> error(String message, HttpStatus status, Object errors) {
        var response = new ApiResponse<Void>(false, message, null, errors);
        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<ApiResponse<Void>> badRequest(String message) {
        var response = new ApiResponse<Void>(false, message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
