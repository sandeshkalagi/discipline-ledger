package com.disciplineledger.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class TradeResponse {
    private Long id;
    private Long userId;
    private Long journalEntryId;
    private LocalDate tradeDate;
    private String symbol;
    private String side;
    private BigDecimal quantity;
    private BigDecimal entryPrice;
    private BigDecimal exitPrice;
    private BigDecimal profitLoss;
    private String strategy;
    private BigDecimal setupQuality;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
