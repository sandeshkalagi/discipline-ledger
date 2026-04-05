package com.disciplineledger.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class JournalEntryRequest {
    @NotNull
    private LocalDate entryDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal startingCapital;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal endingCapital;

    @NotNull
    @Min(0)
    private Integer numberOfTrades;

    private String mistakesMade;
    private String whatWentWell;
    private String notes;
    private String moodDisciplineNotes;
}
