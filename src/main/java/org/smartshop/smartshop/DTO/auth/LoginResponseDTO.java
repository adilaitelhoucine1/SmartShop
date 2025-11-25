package org.smartshop.smartshop.DTO.auth;

import lombok.Data;
import org.smartshop.smartshop.enums.UserRole;

@Data
public class LoginResponseDTO {
    private Long userId;
    private String username;
    private UserRole role;
    private String message;
    
    public LoginResponseDTO(Long userId, String username, UserRole role, String message) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.message = message;
    }
}