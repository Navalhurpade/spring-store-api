package com.naval.store.mappers;

import com.naval.store.dtos.users.RegisterUserRequest;
import com.naval.store.dtos.users.UpdateUserRequest;
import com.naval.store.dtos.users.UserDto;
import com.naval.store.entities.User;
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
