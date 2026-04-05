package com.disciplineledger.service;

import com.disciplineledger.dto.CapitalGrowthPoint;
import com.disciplineledger.dto.StatisticsResponse;
import com.disciplineledger.entity.JournalEntry;
import com.disciplineledger.entity.Trade;
import com.disciplineledger.repository.JournalEntryRepository;
import com.disciplineledger.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final JournalEntryRepository journalEntryRepository;
    private final TradeRepository tradeRepository;

    @Transactional(readOnly = true)
    public StatisticsResponse getStatistics(Long userId) {
        List<JournalEntry> entries = journalEntryRepository.findAllByUserIdOrderByEntryDateAsc(userId);
        if (entries.isEmpty()) {
            return StatisticsResponse.builder()
                    .userId(userId)
                    .totalDays(0)
                    .totalTrades(0)
                    .totalTradeRecords(0)
                    .winRate(ZERO)
                    .averageProfit(ZERO)
                    .averageLoss(ZERO)
                    .tradeWinRate(ZERO)
                    .averageTradeProfit(ZERO)
                    .averageTradeLoss(ZERO)
                    .expectancy(ZERO)
                    .bestDay(ZERO)
                    .worstDay(ZERO)
                    .capitalGrowthHistory(List.of())
                    .build();
        }

        int totalDays = entries.size();
        int totalTrades = entries.stream().mapToInt(JournalEntry::getNumberOfTrades).sum();
        List<BigDecimal> pnlValues = entries.stream().map(JournalEntry::getTodaysProfitLoss).toList();

        List<BigDecimal> wins = pnlValues.stream().filter(pnl -> pnl.compareTo(BigDecimal.ZERO) > 0).toList();
        List<BigDecimal> losses = pnlValues.stream().filter(pnl -> pnl.compareTo(BigDecimal.ZERO) < 0).toList();

        BigDecimal winRate = BigDecimal.valueOf(wins.size())
                .multiply(HUNDRED)
                .divide(BigDecimal.valueOf(totalDays), 2, RoundingMode.HALF_UP);

        BigDecimal averageProfit = wins.isEmpty()
                ? ZERO
                : wins.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(wins.size()), 2, RoundingMode.HALF_UP);

        BigDecimal averageLoss = losses.isEmpty()
                ? ZERO
                : losses.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(losses.size()), 2, RoundingMode.HALF_UP);

        BigDecimal bestDay = pnlValues.stream().max(BigDecimal::compareTo).orElse(ZERO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal worstDay = pnlValues.stream().min(BigDecimal::compareTo).orElse(ZERO).setScale(2, RoundingMode.HALF_UP);

        List<Trade> trades = tradeRepository.findAllByUserIdOrderByTradeDateAscCreatedAtAsc(userId);
        int totalTradeRecords = trades.size();
        List<BigDecimal> tradePnls = trades.stream().map(Trade::getProfitLoss).toList();
        List<BigDecimal> tradeWins = tradePnls.stream().filter(p -> p.compareTo(BigDecimal.ZERO) > 0).toList();
        List<BigDecimal> tradeLosses = tradePnls.stream().filter(p -> p.compareTo(BigDecimal.ZERO) < 0).toList();

        BigDecimal tradeWinRate = totalTradeRecords == 0
                ? ZERO
                : BigDecimal.valueOf(tradeWins.size())
                .multiply(HUNDRED)
                .divide(BigDecimal.valueOf(totalTradeRecords), 2, RoundingMode.HALF_UP);

        BigDecimal averageTradeProfit = tradeWins.isEmpty()
                ? ZERO
                : tradeWins.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(tradeWins.size()), 2, RoundingMode.HALF_UP);

        BigDecimal averageTradeLoss = tradeLosses.isEmpty()
                ? ZERO
                : tradeLosses.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(tradeLosses.size()), 2, RoundingMode.HALF_UP);

        BigDecimal expectancy = totalTradeRecords == 0
                ? ZERO
                : tradePnls.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(totalTradeRecords), 2, RoundingMode.HALF_UP);

        List<CapitalGrowthPoint> growthHistory = entries.stream()
                .map(entry -> CapitalGrowthPoint.builder()
                        .entryDate(entry.getEntryDate())
                        .endingCapital(entry.getEndingCapital().setScale(2, RoundingMode.HALF_UP))
                        .build())
                .toList();

        return StatisticsResponse.builder()
                .userId(userId)
                .totalDays(totalDays)
                .totalTrades(totalTrades)
                .totalTradeRecords(totalTradeRecords)
                .winRate(winRate)
                .averageProfit(averageProfit)
                .averageLoss(averageLoss)
                .tradeWinRate(tradeWinRate)
                .averageTradeProfit(averageTradeProfit)
                .averageTradeLoss(averageTradeLoss)
                .expectancy(expectancy)
                .bestDay(bestDay)
                .worstDay(worstDay)
                .capitalGrowthHistory(growthHistory)
                .build();
    }
}
