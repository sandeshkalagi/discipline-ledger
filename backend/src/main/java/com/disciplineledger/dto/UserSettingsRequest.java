package com.disciplineledger.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserSettingsRequest {

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal initialCapital;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal targetReturnPercentage;

    @NotNull
    @Min(1)
    private Integer maxTradesPerDay;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal maxDailyLossPercentage;

    private String motivationMessage;
}
