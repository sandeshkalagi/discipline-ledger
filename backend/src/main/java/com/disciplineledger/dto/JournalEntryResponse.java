package com.disciplineledger.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class JournalEntryResponse {
    private Long id;
    private Long userId;
    private LocalDate entryDate;
    private BigDecimal startingCapital;
    private BigDecimal endingCapital;
    private BigDecimal todaysProfitLoss;
    private Integer numberOfTrades;
    private String mistakesMade;
    private String whatWentWell;
    private String notes;
    private String moodDisciplineNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
