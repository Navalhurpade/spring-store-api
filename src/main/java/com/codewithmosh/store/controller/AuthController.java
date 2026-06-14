package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.auth.JwtResponse;
import com.codewithmosh.store.dtos.auth.LoginRequest;
import com.codewithmosh.store.dtos.users.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            throw new BadCredentialsException("Invalid credentials");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getId().toString(),
                        request.getPassword()
                )
        );
        var accessToken = jwtService.generateAccessToken(user).toString();
        var refreshToken = jwtService.generateRefreshToken(user).toString();

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(604800); //7d
        cookie.setPath("/auth/refresh");

        response.addCookie(cookie);
        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var jwt = jwtService.parse(refreshToken);
        if (!jwt.isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = jwtService.parse(refreshToken).getUserId();
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user).toString();

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @GetMapping("/me")
    public UserDto getUser() {
        var ctx = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) ctx.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);

        return userMapper.toDto(user);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadRequest() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
