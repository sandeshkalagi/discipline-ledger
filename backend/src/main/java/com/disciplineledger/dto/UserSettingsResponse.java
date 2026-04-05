package com.disciplineledger.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UserSettingsResponse {
    private Long userId;
    private BigDecimal initialCapital;
    private BigDecimal targetReturnPercentage;
    private Integer maxTradesPerDay;
    private BigDecimal maxDailyLossPercentage;
    private String motivationMessage;
}
