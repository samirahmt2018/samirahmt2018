package com.gundan.terragold.config;

import com.gundan.terragold.entity.AppUser;
import com.gundan.terragold.enums.UserRole;
import com.gundan.terragold.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDefaultUser(UserRepository userRepository) {
        return args -> {
            // Check if the user already exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                AppUser defaultAdmin = AppUser.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123")) // always encode
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(defaultAdmin);
                System.out.println("Default admin user created: admin / admin123");
            }
        };
    }
}
