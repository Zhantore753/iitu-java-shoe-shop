package com.example.shoeshop.seed;

import com.example.shoeshop.model.User;
import com.example.shoeshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeed implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Override
    public void run(String... args) {
        if (userRepository.findByRole(User.Role.ADMIN).isEmpty()) {
            String adminUsername = environment.getProperty("spring.security.admin.name");
            String adminPassword = environment.getProperty("spring.security.admin.password");
            String adminEmail = environment.getProperty("spring.security.admin.email");

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setRole(User.Role.ADMIN);

            userRepository.save(admin);
            System.out.println("Admin user created: " + adminUsername);
        }
    }

}
