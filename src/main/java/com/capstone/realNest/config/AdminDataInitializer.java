package com.capstone.realNest.config;

import com.capstone.realNest.entity.User;
import com.capstone.realNest.enums.Role;
import com.capstone.realNest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${realnest.admin.name}")
    private String adminName;

    @Value("${realnest.admin.email}")
    private String adminEmail;

    @Value("${realnest.admin.password}")
    private String adminPassword;

    @Value("${realnest.admin.phone}")
    private String adminPhone;


    // Creates the initial Admin account only when its email does not exist.
    @Override
    public void run(String... args) {

        String normalizedEmail = adminEmail.trim().toLowerCase(Locale.ROOT);

        String normalizedPhone = adminPhone.trim();

        if (userRepository.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            LOGGER.info("RealNest Admin account already exists: {}", normalizedEmail);

            return;
        }

        if (userRepository.existsByPhone(normalizedPhone)) {
            throw new IllegalStateException("Cannot create the RealNest Admin " + "because its configured phone " + "number already belongs to " + "another account");
        }

        User admin = new User();

        admin.setName(adminName.trim());
        admin.setEmail(normalizedEmail);
        admin.setPhone(normalizedPhone);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);

        LOGGER.info("RealNest Admin account created: {}", normalizedEmail);
    }
}