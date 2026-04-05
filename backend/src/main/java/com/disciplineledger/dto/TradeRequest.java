package com.disciplineledger.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TradeRequest {

    @NotNull
    private Long journalEntryId;

    @NotNull
    private LocalDate tradeDate;

    @NotBlank
    private String symbol;

    @NotBlank
    private String side;

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)
    private BigDecimal entryPrice;

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)
    private BigDecimal exitPrice;

    private String strategy;
    private BigDecimal setupQuality;
    private String notes;
}
