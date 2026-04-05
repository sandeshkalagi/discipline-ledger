package com.disciplineledger.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class CapitalGrowthPoint {
    private LocalDate entryDate;
    private BigDecimal endingCapital;
}
