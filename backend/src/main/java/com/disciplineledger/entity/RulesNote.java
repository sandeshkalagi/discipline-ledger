package com.disciplineledger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rules_notes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RulesNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "trading_rules", columnDefinition = "TEXT")
    private String tradingRules;

    @Column(name = "discipline_rules", columnDefinition = "TEXT")
    private String disciplineRules;

    @Column(name = "motivation_notes", columnDefinition = "TEXT")
    private String motivationNotes;

    @Column(name = "daily_reminders", columnDefinition = "TEXT")
    private String dailyReminders;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
