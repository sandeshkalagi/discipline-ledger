package com.disciplineledger.service;

import com.disciplineledger.dto.DashboardResponse;
import com.disciplineledger.entity.JournalEntry;
import com.disciplineledger.entity.UserSettings;
import com.disciplineledger.repository.JournalEntryRepository;
import com.disciplineledger.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final JournalEntryRepository journalEntryRepository;
    private final UserSettingsRepository userSettingsRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(Long userId) {
        JournalEntry latestEntry = journalEntryRepository.findFirstByUserIdOrderByEntryDateDesc(userId).orElse(null);
        JournalEntry firstEntry = journalEntryRepository.findFirstByUserIdOrderByEntryDateAsc(userId).orElse(null);
        UserSettings settings = userSettingsRepository.findByUserId(userId).orElse(null);

        BigDecimal initialCapital = resolveInitialCapital(settings, firstEntry);
        BigDecimal currentCapital = latestEntry != null ? latestEntry.getEndingCapital() : initialCapital;
        BigDecimal todayProfitLoss = latestEntry != null ? latestEntry.getTodaysProfitLoss() : ZERO;
        BigDecimal totalProfitLoss = currentCapital.subtract(initialCapital);
        BigDecimal totalReturnPercentage = calculateReturnPercent(initialCapital, currentCapital);

        BigDecimal targetReturnPercentage = settings != null ? settings.getTargetReturnPercentage() : new BigDecimal("10.00");
        BigDecimal targetCapital = initialCapital.add(initialCapital.multiply(targetReturnPercentage).divide(HUNDRED, 2, RoundingMode.HALF_UP));
        BigDecimal remainingToTarget = targetCapital.subtract(currentCapital).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

        List<String> warnings = buildWarnings(latestEntry, settings, targetCapital);
        String motivationMessage = settings != null && settings.getMotivationMessage() != null
                ? settings.getMotivationMessage()
                : "Protect capital first. Consistency over intensity.";

        return DashboardResponse.builder()
                .userId(userId)
                .initialCapital(initialCapital.setScale(2, RoundingMode.HALF_UP))
                .currentCapital(currentCapital.setScale(2, RoundingMode.HALF_UP))
                .todayProfitLoss(todayProfitLoss.setScale(2, RoundingMode.HALF_UP))
                .totalProfitLoss(totalProfitLoss.setScale(2, RoundingMode.HALF_UP))
                .totalReturnPercentage(totalReturnPercentage)
                .targetReturnPercentage(targetReturnPercentage.setScale(2, RoundingMode.HALF_UP))
                .targetCapital(targetCapital.setScale(2, RoundingMode.HALF_UP))
                .remainingToTarget(remainingToTarget)
                .warnings(warnings)
                .motivationMessage(motivationMessage)
                .build();
    }

    private BigDecimal resolveInitialCapital(UserSettings settings, JournalEntry firstEntry) {
        if (settings != null && settings.getInitialCapital() != null) {
            return settings.getInitialCapital();
        }
        if (firstEntry != null && firstEntry.getStartingCapital() != null) {
            return firstEntry.getStartingCapital();
        }
        return ZERO;
    }

    private BigDecimal calculateReturnPercent(BigDecimal initialCapital, BigDecimal currentCapital) {
        if (initialCapital == null || initialCapital.compareTo(BigDecimal.ZERO) <= 0) {
            return ZERO;
        }
        return currentCapital.subtract(initialCapital)
                .divide(initialCapital, 6, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private List<String> buildWarnings(JournalEntry latestEntry, UserSettings settings, BigDecimal targetCapital) {
        List<String> warnings = new ArrayList<>();
        if (latestEntry == null) {
            return warnings;
        }

        int maxTrades = settings != null && settings.getMaxTradesPerDay() != null ? settings.getMaxTradesPerDay() : 8;
        BigDecimal maxDailyLossPercentage = settings != null && settings.getMaxDailyLossPercentage() != null
                ? settings.getMaxDailyLossPercentage()
                : new BigDecimal("2.00");

        if (latestEntry.getNumberOfTrades() != null && latestEntry.getNumberOfTrades() > maxTrades) {
            warnings.add("Don't overtrade. You are above your trade limit.");
        }

        BigDecimal dailyLossLimit = latestEntry.getStartingCapital()
                .multiply(maxDailyLossPercentage)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP)
                .negate();
        if (latestEntry.getTodaysProfitLoss().compareTo(dailyLossLimit) <= 0) {
            warnings.add("Stop trading. Daily loss limit reached.");
        }

        if (latestEntry.getEndingCapital().compareTo(targetCapital) >= 0) {
            warnings.add("Target reached. Stop trading for today.");
        }

        return warnings;
    }
}
