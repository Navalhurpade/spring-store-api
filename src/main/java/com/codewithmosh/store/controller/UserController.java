package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.users.RegisterUserRequest;
import com.codewithmosh.store.dtos.users.UpdateUserRequest;
import com.codewithmosh.store.dtos.users.UserDto;
import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getUsers(@RequestParam(name = "sort", required = false) String sort) {

        if (!Set.of("name", "email").contains(sort)) {
            sort = "name";
        }
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var data = userRepository.findById(id).orElse(null);

        if (data == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(data));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var found = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (found != null) {
            return ResponseEntity.notFound().build();
        }
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        var data = userRepository.save(user);
        var userDto = userMapper.toDto(data);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        var found = userRepository.findById(id).orElse(null);

        if (found == null) {
            return ResponseEntity.notFound().build();
        }

        userMapper.toRequestUpdate(request, found);

        var data = userRepository.save(found);
        return ResponseEntity.ok(userMapper.toDto(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var found = userRepository.findById(id).orElse(null);

        if (found == null) {
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(found);
        return ResponseEntity.noContent().build();
    }
}
