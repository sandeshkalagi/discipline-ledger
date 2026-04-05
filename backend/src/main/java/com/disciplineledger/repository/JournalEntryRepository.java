package com.disciplineledger.repository;

import com.disciplineledger.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findAllByUserIdOrderByEntryDateDesc(Long userId);
    List<JournalEntry> findAllByUserIdOrderByEntryDateAsc(Long userId);
    Optional<JournalEntry> findFirstByUserIdOrderByEntryDateDesc(Long userId);
    Optional<JournalEntry> findFirstByUserIdOrderByEntryDateAsc(Long userId);
}
