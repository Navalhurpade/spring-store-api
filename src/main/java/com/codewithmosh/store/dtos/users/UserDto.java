package com.codewithmosh.store.dtos.users;

import com.codewithmosh.store.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String name;
    private Role role;
    private LocalDateTime createdAt;
}
