package com.Croquetas.initializer;

import com.Croquetas.model.User;
import com.Croquetas.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("elgatodelola"));
            admin.setRole("ROLE_ADMIN");
            admin.setApproved(true);
            userRepository.save(admin);
            System.out.println("Admin user created: admin / admin123");
        }
        if (userRepository.findByUsername("testchef").isEmpty()) {
            User chef = new User();
            chef.setUsername("testchef");
            chef.setPassword(passwordEncoder.encode("chef123"));
            chef.setRole("ROLE_CHEF");
            chef.setApproved(false);
            userRepository.save(chef);
        }
    }
}