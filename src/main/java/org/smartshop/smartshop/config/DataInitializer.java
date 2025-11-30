package org.smartshop.smartshop.config;

import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.entity.User;
import org.smartshop.smartshop.enums.UserRole;
import org.smartshop.smartshop.repository.ConfigRepository;
import org.smartshop.smartshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();;
    private final ConfigRepository configRepository;
    @Override
    public void run(String... args) {
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
//        ConfigService configService= new ConfigService(configRepository);
//        System.out.println("TVA: " + configService.getTva());

        if (!userRepository.existsByRole(UserRole.ADMIN)) {
            User admin = User.builder()
                    .username("admin@youcode.ma")
                        .password(passwordEncoder.encode("admin123"))
                    .role(UserRole.ADMIN)
                    .build();
            
            userRepository.save(admin);
        }
    }
}
