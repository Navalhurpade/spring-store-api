package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.users.RegisterUserRequest;
import com.codewithmosh.store.dtos.users.UpdateUserRequest;
import com.codewithmosh.store.dtos.users.UserDto;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())", dateFormat = "MMM DD YYYY")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request);

    void toRequestUpdate(UpdateUserRequest request, @MappingTarget User user);

    List<UserDto> toUsersDto(List<User> users);
}
