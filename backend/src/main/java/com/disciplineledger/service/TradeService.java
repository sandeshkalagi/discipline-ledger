package com.disciplineledger.service;

import com.disciplineledger.dto.TradeRequest;
import com.disciplineledger.dto.TradeResponse;
import com.disciplineledger.entity.JournalEntry;
import com.disciplineledger.entity.Trade;
import com.disciplineledger.repository.JournalEntryRepository;
import com.disciplineledger.repository.TradeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final JournalEntryRepository journalEntryRepository;

    @Transactional
    public TradeResponse createTrade(Long userId, TradeRequest request) {
        validateJournalEntryOwnership(userId, request.getJournalEntryId());

        LocalDateTime now = LocalDateTime.now();
        Trade trade = Trade.builder()
                .userId(userId)
                .journalEntryId(request.getJournalEntryId())
                .tradeDate(request.getTradeDate())
                .symbol(request.getSymbol().trim().toUpperCase())
                .side(normalizeSide(request.getSide()))
                .quantity(request.getQuantity())
                .entryPrice(request.getEntryPrice())
                .exitPrice(request.getExitPrice())
                .profitLoss(calculateProfitLoss(request.getSide(), request.getQuantity(), request.getEntryPrice(), request.getExitPrice()))
                .strategy(request.getStrategy())
                .setupQuality(request.getSetupQuality())
                .notes(request.getNotes())
                .createdAt(now)
                .updatedAt(now)
                .build();
        return toResponse(tradeRepository.save(trade));
    }

    @Transactional(readOnly = true)
    public List<TradeResponse> getTrades(Long userId, Long journalEntryId) {
        List<Trade> trades = journalEntryId == null
                ? tradeRepository.findAllByUserIdOrderByTradeDateDescCreatedAtDesc(userId)
                : tradeRepository.findAllByUserIdAndJournalEntryIdOrderByTradeDateDescCreatedAtDesc(userId, journalEntryId);
        return trades.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TradeResponse getTradeById(Long userId, Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new EntityNotFoundException("Trade not found: " + tradeId));
        if (!trade.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Trade not found: " + tradeId);
        }
        return toResponse(trade);
    }

    @Transactional
    public TradeResponse updateTrade(Long userId, Long tradeId, TradeRequest request) {
        Trade existing = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new EntityNotFoundException("Trade not found: " + tradeId));
        if (!existing.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Trade not found: " + tradeId);
        }
        validateJournalEntryOwnership(userId, request.getJournalEntryId());

        existing.setJournalEntryId(request.getJournalEntryId());
        existing.setTradeDate(request.getTradeDate());
        existing.setSymbol(request.getSymbol().trim().toUpperCase());
        existing.setSide(normalizeSide(request.getSide()));
        existing.setQuantity(request.getQuantity());
        existing.setEntryPrice(request.getEntryPrice());
        existing.setExitPrice(request.getExitPrice());
        existing.setProfitLoss(calculateProfitLoss(request.getSide(), request.getQuantity(), request.getEntryPrice(), request.getExitPrice()));
        existing.setStrategy(request.getStrategy());
        existing.setSetupQuality(request.getSetupQuality());
        existing.setNotes(request.getNotes());
        existing.setUpdatedAt(LocalDateTime.now());

        return toResponse(tradeRepository.save(existing));
    }

    @Transactional
    public void deleteTrade(Long userId, Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new EntityNotFoundException("Trade not found: " + tradeId));
        if (!trade.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Trade not found: " + tradeId);
        }
        tradeRepository.deleteById(tradeId);
    }

    private void validateJournalEntryOwnership(Long userId, Long journalEntryId) {
        JournalEntry entry = journalEntryRepository.findById(journalEntryId)
                .orElseThrow(() -> new EntityNotFoundException("Journal entry not found: " + journalEntryId));
        if (!entry.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Journal entry not found: " + journalEntryId);
        }
    }

    private String normalizeSide(String side) {
        String normalized = side.trim().toUpperCase();
        if (!normalized.equals("BUY") && !normalized.equals("SELL")) {
            throw new IllegalArgumentException("Side must be BUY or SELL");
        }
        return normalized;
    }

    private BigDecimal calculateProfitLoss(String side, BigDecimal qty, BigDecimal entry, BigDecimal exit) {
        String normalized = normalizeSide(side);
        BigDecimal diff = normalized.equals("BUY") ? exit.subtract(entry) : entry.subtract(exit);
        return diff.multiply(qty).setScale(2, RoundingMode.HALF_UP);
    }

    private TradeResponse toResponse(Trade trade) {
        return TradeResponse.builder()
                .id(trade.getId())
                .userId(trade.getUserId())
                .journalEntryId(trade.getJournalEntryId())
                .tradeDate(trade.getTradeDate())
                .symbol(trade.getSymbol())
                .side(trade.getSide())
                .quantity(trade.getQuantity())
                .entryPrice(trade.getEntryPrice())
                .exitPrice(trade.getExitPrice())
                .profitLoss(trade.getProfitLoss())
                .strategy(trade.getStrategy())
                .setupQuality(trade.getSetupQuality())
                .notes(trade.getNotes())
                .createdAt(trade.getCreatedAt())
                .updatedAt(trade.getUpdatedAt())
                .build();
    }
}
