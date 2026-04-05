package com.disciplineledger.service;

import com.disciplineledger.dto.JournalEntryRequest;
import com.disciplineledger.dto.JournalEntryResponse;
import com.disciplineledger.entity.JournalEntry;
import com.disciplineledger.repository.JournalEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    @Transactional
    public JournalEntryResponse createEntry(Long userId, JournalEntryRequest request) {
        JournalEntry entry = JournalEntry.builder()
                .userId(userId)
                .entryDate(request.getEntryDate())
                .startingCapital(request.getStartingCapital())
                .endingCapital(request.getEndingCapital())
                .todaysProfitLoss(calculateTodaysProfitLoss(request.getStartingCapital(), request.getEndingCapital()))
                .numberOfTrades(request.getNumberOfTrades())
                .mistakesMade(request.getMistakesMade())
                .whatWentWell(request.getWhatWentWell())
                .notes(request.getNotes())
                .moodDisciplineNotes(request.getMoodDisciplineNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return toResponse(journalEntryRepository.save(entry));
    }

    @Transactional(readOnly = true)
    public List<JournalEntryResponse> getEntriesByUser(Long userId) {
        return journalEntryRepository.findAllByUserIdOrderByEntryDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public JournalEntryResponse getEntryById(Long userId, Long id) {
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Journal entry not found: " + id));
        if (!entry.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Journal entry not found: " + id);
        }
        return toResponse(entry);
    }

    @Transactional
    public JournalEntryResponse updateEntry(Long userId, Long id, JournalEntryRequest request) {
        JournalEntry existing = journalEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Journal entry not found: " + id));
        if (!existing.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Journal entry not found: " + id);
        }

        existing.setEntryDate(request.getEntryDate());
        existing.setStartingCapital(request.getStartingCapital());
        existing.setEndingCapital(request.getEndingCapital());
        existing.setTodaysProfitLoss(calculateTodaysProfitLoss(request.getStartingCapital(), request.getEndingCapital()));
        existing.setNumberOfTrades(request.getNumberOfTrades());
        existing.setMistakesMade(request.getMistakesMade());
        existing.setWhatWentWell(request.getWhatWentWell());
        existing.setNotes(request.getNotes());
        existing.setMoodDisciplineNotes(request.getMoodDisciplineNotes());
        existing.setUpdatedAt(LocalDateTime.now());

        return toResponse(journalEntryRepository.save(existing));
    }

    @Transactional
    public void deleteEntry(Long userId, Long id) {
        JournalEntry existing = journalEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Journal entry not found: " + id));
        if (!existing.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Journal entry not found: " + id);
        }
        journalEntryRepository.deleteById(id);
    }

    private BigDecimal calculateTodaysProfitLoss(BigDecimal startingCapital, BigDecimal endingCapital) {
        return endingCapital.subtract(startingCapital);
    }

    private JournalEntryResponse toResponse(JournalEntry entry) {
        return JournalEntryResponse.builder()
                .id(entry.getId())
                .userId(entry.getUserId())
                .entryDate(entry.getEntryDate())
                .startingCapital(entry.getStartingCapital())
                .endingCapital(entry.getEndingCapital())
                .todaysProfitLoss(entry.getTodaysProfitLoss())
                .numberOfTrades(entry.getNumberOfTrades())
                .mistakesMade(entry.getMistakesMade())
                .whatWentWell(entry.getWhatWentWell())
                .notes(entry.getNotes())
                .moodDisciplineNotes(entry.getMoodDisciplineNotes())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();
    }
}
