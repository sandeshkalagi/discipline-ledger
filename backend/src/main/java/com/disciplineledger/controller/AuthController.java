package com.disciplineledger.controller;

import com.disciplineledger.dto.AuthLoginRequest;
import com.disciplineledger.dto.AuthRegisterRequest;
import com.disciplineledger.dto.AuthResponse;
import com.disciplineledger.dto.UserProfileResponse;
import com.disciplineledger.security.SecurityUtil;
import com.disciplineledger.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody AuthRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthLoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserProfileResponse me() {
        return authService.getProfile(SecurityUtil.getCurrentUserId());
    }
}
