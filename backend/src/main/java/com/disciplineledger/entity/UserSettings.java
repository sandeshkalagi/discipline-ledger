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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "initial_capital", nullable = false, precision = 19, scale = 2)
    private BigDecimal initialCapital;

    @Column(name = "target_return_percentage", nullable = false, precision = 7, scale = 2)
    private BigDecimal targetReturnPercentage;

    @Column(name = "max_trades_per_day", nullable = false)
    private Integer maxTradesPerDay;

    @Column(name = "max_daily_loss_percentage", nullable = false, precision = 7, scale = 2)
    private BigDecimal maxDailyLossPercentage;

    @Column(name = "motivation_message", columnDefinition = "TEXT")
    private String motivationMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
