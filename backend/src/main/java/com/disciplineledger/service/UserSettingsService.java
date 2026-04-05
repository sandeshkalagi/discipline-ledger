package com.disciplineledger.service;

import com.disciplineledger.dto.UserSettingsRequest;
import com.disciplineledger.dto.UserSettingsResponse;
import com.disciplineledger.entity.UserSettings;
import com.disciplineledger.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    @Transactional(readOnly = true)
    public UserSettingsResponse getSettings(Long userId) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));
        return toResponse(settings);
    }

    @Transactional
    public UserSettingsResponse upsertSettings(Long userId, UserSettingsRequest request) {
        UserSettings settings = userSettingsRepository.findByUserId(userId).orElse(null);
        LocalDateTime now = LocalDateTime.now();

        if (settings == null) {
            settings = UserSettings.builder()
                    .userId(userId)
                    .createdAt(now)
                    .build();
        }

        settings.setInitialCapital(request.getInitialCapital());
        settings.setTargetReturnPercentage(request.getTargetReturnPercentage());
        settings.setMaxTradesPerDay(request.getMaxTradesPerDay());
        settings.setMaxDailyLossPercentage(request.getMaxDailyLossPercentage());
        settings.setMotivationMessage(request.getMotivationMessage());
        settings.setUpdatedAt(now);

        return toResponse(userSettingsRepository.save(settings));
    }

    private UserSettings createDefaultSettings(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        UserSettings settings = UserSettings.builder()
                .userId(userId)
                .initialCapital(new BigDecimal("100000.00"))
                .targetReturnPercentage(new BigDecimal("10.00"))
                .maxTradesPerDay(8)
                .maxDailyLossPercentage(new BigDecimal("2.00"))
                .motivationMessage("Protect capital first. Consistency over intensity.")
                .createdAt(now)
                .updatedAt(now)
                .build();
        return userSettingsRepository.save(settings);
    }

    private UserSettingsResponse toResponse(UserSettings settings) {
        return UserSettingsResponse.builder()
                .userId(settings.getUserId())
                .initialCapital(settings.getInitialCapital())
                .targetReturnPercentage(settings.getTargetReturnPercentage())
                .maxTradesPerDay(settings.getMaxTradesPerDay())
                .maxDailyLossPercentage(settings.getMaxDailyLossPercentage())
                .motivationMessage(settings.getMotivationMessage())
                .build();
    }
}
