package com.example.events.controllers;
import com.example.events.dto.AuthResponseDto;
import com.example.events.dto.LoginDto;
import com.example.events.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authenticationService.authenticate(loginDto), HttpStatus.OK);
    }

    @PostMapping("refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.refreshToken(request), HttpStatus.OK);
    }
}
