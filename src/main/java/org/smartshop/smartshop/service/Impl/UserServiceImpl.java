package org.smartshop.smartshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.DTO.auth.LoginRequestDTO;
import org.smartshop.smartshop.DTO.auth.LoginResponseDTO;
import org.smartshop.smartshop.entity.User;
import org.smartshop.smartshop.exception.UnauthorizedException;
import org.smartshop.smartshop.repository.UserRepository;
import org.smartshop.smartshop.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder(); ;
    

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {

        User user = userRepository.findByUsername(loginRequest.getUsername());
        
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponseDTO(user.getId(), user.getUsername(), user.getRole(), "Connexion réussie");

        }
        
        throw new UnauthorizedException("mot de passe ou email pas valide");
    }
}