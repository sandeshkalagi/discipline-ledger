package com.disciplineledger.controller;

import com.disciplineledger.dto.UserSettingsRequest;
import com.disciplineledger.dto.UserSettingsResponse;
import com.disciplineledger.security.SecurityUtil;
import com.disciplineledger.service.UserSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @GetMapping
    public UserSettingsResponse getSettings() {
        return userSettingsService.getSettings(SecurityUtil.getCurrentUserId());
    }

    @PutMapping
    public UserSettingsResponse updateSettings(@Valid @RequestBody UserSettingsRequest request) {
        return userSettingsService.upsertSettings(SecurityUtil.getCurrentUserId(), request);
    }
}
