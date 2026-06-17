package com.naval.store.controller;

import com.naval.store.dtos.apiResponse.ApiResponse;
import com.naval.store.dtos.users.RegisterUserRequest;
import com.naval.store.dtos.users.UpdateUserRequest;
import com.naval.store.dtos.users.UserDto;
import com.naval.store.mappers.UserMapper;
import com.naval.store.services.UserService;
import com.naval.store.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ResponseUtils responseUtils;
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsers(@RequestParam(name = "sort", required = false, defaultValue = "name") String sort) {
        var users = userService.getUsers(sort);

        return responseUtils.ok(userMapper.toUsersDto(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        var user = userService.getUserById(id);
        return responseUtils.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(
            @RequestBody @Valid RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var user = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        return responseUtils.ok(userMapper.toDto(user), HttpStatus.CREATED, uri);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        var user = userService.updateUser(id, request);
        return responseUtils.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return responseUtils.ok(null, HttpStatus.NO_CONTENT, null);
    }
}
