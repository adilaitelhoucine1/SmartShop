package org.smartshop.smartshop.service;

import org.smartshop.smartshop.DTO.auth.LoginRequestDTO;
import org.smartshop.smartshop.DTO.auth.LoginResponseDTO;

public interface UserService {
     LoginResponseDTO authenticate(LoginRequestDTO loginRequest);
}
