package com.example.events.service;


import com.example.events.enums.Role;
import com.example.events.models.AppUser;
import com.example.events.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void createAdmin(String email, String inputPassword) {
        if (appUserRepository.findByUsername(email).isPresent()) {
            return;
        }

        AppUser admin = new AppUser();
        admin.setUsername(email);
        admin.setPassword(passwordEncoder.encode(inputPassword));
        admin.setRole(Role.ADMIN);
        appUserRepository.save(admin);
    }
}
