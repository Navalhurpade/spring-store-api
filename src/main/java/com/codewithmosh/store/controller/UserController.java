package com.codewithmosh.store.controller;

import com.codewithmosh.store.dtos.users.RegisterUserRequest;
import com.codewithmosh.store.dtos.users.UpdateUserRequest;
import com.codewithmosh.store.dtos.users.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getUsers(@RequestParam(value = "", name = "sort", required = false) String sort) {

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
            @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var found = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (found != null) {
            return ResponseEntity.notFound().build();
        }

        var data = userRepository.save(userMapper.toEntity(request));
        var userDto = userMapper.toDto(data);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
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
