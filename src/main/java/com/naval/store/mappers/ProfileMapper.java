package com.naval.store.mappers;

import com.naval.store.dtos.profile.ProfileDto;
import com.naval.store.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDto toDto(Profile profile);
}
