package com.disciplineledger.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class StatisticsResponse {
    private Long userId;
    private Integer totalDays;
    private Integer totalTrades;
    private Integer totalTradeRecords;
    private BigDecimal winRate;
    private BigDecimal averageProfit;
    private BigDecimal averageLoss;
    private BigDecimal tradeWinRate;
    private BigDecimal averageTradeProfit;
    private BigDecimal averageTradeLoss;
    private BigDecimal expectancy;
    private BigDecimal bestDay;
    private BigDecimal worstDay;
    private List<CapitalGrowthPoint> capitalGrowthHistory;
}
