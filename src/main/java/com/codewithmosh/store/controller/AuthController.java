package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.auth.LoginRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadRequest() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
