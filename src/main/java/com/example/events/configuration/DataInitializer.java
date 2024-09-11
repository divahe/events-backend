package com.example.events.configuration;

import com.example.events.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final AdminService adminService;

    @Value("${administration.username}")
    private String adminUsername;

    @Value("${administration.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        adminService.createAdmin(adminUsername, adminPassword);
    }
}
