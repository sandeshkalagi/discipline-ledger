package com.disciplineledger.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class DashboardResponse {
    private Long userId;
    private BigDecimal initialCapital;
    private BigDecimal currentCapital;
    private BigDecimal todayProfitLoss;
    private BigDecimal totalProfitLoss;
    private BigDecimal totalReturnPercentage;
    private BigDecimal targetReturnPercentage;
    private BigDecimal targetCapital;
    private BigDecimal remainingToTarget;
    private String motivationMessage;
    private List<String> warnings;
}
