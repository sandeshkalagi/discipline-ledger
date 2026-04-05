package com.disciplineledger.repository;

import com.disciplineledger.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findAllByUserIdOrderByTradeDateDescCreatedAtDesc(Long userId);
    List<Trade> findAllByUserIdAndJournalEntryIdOrderByTradeDateDescCreatedAtDesc(Long userId, Long journalEntryId);
    List<Trade> findAllByUserIdOrderByTradeDateAscCreatedAtAsc(Long userId);
}
