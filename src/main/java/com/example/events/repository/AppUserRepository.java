package com.example.events.repository;

import com.example.events.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByIdCode(String idCode);
}
