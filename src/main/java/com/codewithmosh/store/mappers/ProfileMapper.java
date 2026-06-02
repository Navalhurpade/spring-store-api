package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.profile.ProfileDto;
import com.codewithmosh.store.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDto toDto(Profile profile);
}
