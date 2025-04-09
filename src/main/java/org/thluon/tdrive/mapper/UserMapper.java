package org.thluon.tdrive.mapper;

import com.fasterxml.uuid.Generators;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.thluon.tdrive.dto.RegistrationRequestDTO;
import org.thluon.tdrive.entity.UserDetailsImpl;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", expression = "java(UserMapper.timeBasedUuid())")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "lockReason", ignore = true)
    @Mapping(target = "isLocked", ignore = true)
    @Mapping(target = "hashedPassword", source = "password")
    UserDetailsImpl toUserDetails(RegistrationRequestDTO request);
    static UUID timeBasedUuid() {
        return Generators.timeBasedGenerator().generate();
    }
}
