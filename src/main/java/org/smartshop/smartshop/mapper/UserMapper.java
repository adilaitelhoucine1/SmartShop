package org.smartshop.smartshop.mapper;

import org.mapstruct.*;
import org.smartshop.smartshop.DTO.auth.LoginRequestDTO;
import org.smartshop.smartshop.DTO.auth.LoginResponseDTO;
import org.smartshop.smartshop.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "client", ignore = true)
    User toEntity(LoginRequestDTO dto);

    @Mapping(source = "id", target = "userId")
    @Mapping(target = "message", ignore = true)
    LoginResponseDTO toLoginResponseDTO(User entity);
}