package org.smartshop.smartshop.mapper;

import org.smartshop.smartshop.DTO.auth.LoginRequestDTO;
import org.smartshop.smartshop.DTO.auth.LoginResponseDTO;
import org.smartshop.smartshop.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(LoginRequestDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    public LoginResponseDTO toLoginResponseDTO(User entity, String message) {
        return new LoginResponseDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getRole(),
                message
        );
    }
}