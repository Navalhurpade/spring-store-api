package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.apiResponse.ApiResponse;
import com.codewithmosh.store.exceptions.auth.ResourceForbiddenException;
import com.codewithmosh.store.exceptions.auth.UserNotFoundException;
import com.codewithmosh.store.exceptions.cart.CartNotFoundException;
import com.codewithmosh.store.exceptions.cart.EmptyCartException;
import com.codewithmosh.store.exceptions.product.ProductNotFoundException;
import com.codewithmosh.store.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;

@AllArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    private final ResponseUtils responseUtils;

    //validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationError(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return responseUtils.error("Error in one or more fields", HttpStatus.BAD_REQUEST, errors);
    }

    //Cart Exceptions
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCartNotFound(CartNotFoundException ex) {
        return responseUtils.error(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmptyCart(EmptyCartException ex) {
        return responseUtils.badRequest(ex.getMessage());
    }

    //User Exceptions
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        return responseUtils.error(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    //Product Exceptions
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFound(ProductNotFoundException ex) {
        return responseUtils.error(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    //Security Exceptions
    @ExceptionHandler(ResourceForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbiddenException(ResourceForbiddenException ex) {
        return responseUtils.error(ex.getMessage(), HttpStatus.FORBIDDEN, null);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<Void>> handleUnreadableError() {
        return responseUtils.badRequest("Invalid request body");
    }

}