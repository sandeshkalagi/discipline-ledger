package com.disciplineledger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_entries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "starting_capital", nullable = false, precision = 19, scale = 2)
    private BigDecimal startingCapital;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "ending_capital", nullable = false, precision = 19, scale = 2)
    private BigDecimal endingCapital;

    @NotNull
    @Column(name = "todays_profit_loss", nullable = false, precision = 19, scale = 2)
    private BigDecimal todaysProfitLoss;

    @NotNull
    @Min(0)
    @Column(name = "number_of_trades", nullable = false)
    private Integer numberOfTrades;

    @Column(name = "mistakes_made", columnDefinition = "TEXT")
    private String mistakesMade;

    @Column(name = "what_went_well", columnDefinition = "TEXT")
    private String whatWentWell;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "mood_discipline_notes", columnDefinition = "TEXT")
    private String moodDisciplineNotes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
