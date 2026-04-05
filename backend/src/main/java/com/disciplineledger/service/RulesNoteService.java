package com.disciplineledger.service;

import com.disciplineledger.dto.RulesNoteRequest;
import com.disciplineledger.dto.RulesNoteResponse;
import com.disciplineledger.entity.RulesNote;
import com.disciplineledger.repository.RulesNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RulesNoteService {

    private final RulesNoteRepository rulesNoteRepository;

    @Transactional(readOnly = true)
    public RulesNoteResponse getRulesByUserId(Long userId) {
        RulesNote note = rulesNoteRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyRules(userId));
        return toResponse(note);
    }

    @Transactional
    public RulesNoteResponse upsertRules(Long userId, RulesNoteRequest request) {
        RulesNote existing = rulesNoteRepository.findByUserId(userId).orElse(null);
        LocalDateTime now = LocalDateTime.now();

        if (existing == null) {
            existing = RulesNote.builder()
                    .userId(userId)
                    .createdAt(now)
                    .build();
        }

        existing.setTradingRules(request.getTradingRules());
        existing.setDisciplineRules(request.getDisciplineRules());
        existing.setMotivationNotes(request.getMotivationNotes());
        existing.setDailyReminders(request.getDailyReminders());
        existing.setUpdatedAt(now);

        RulesNote saved = rulesNoteRepository.save(existing);
        return toResponse(saved);
    }

    private RulesNote createEmptyRules(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        RulesNote note = RulesNote.builder()
                .userId(userId)
                .tradingRules("")
                .disciplineRules("")
                .motivationNotes("")
                .dailyReminders("")
                .createdAt(now)
                .updatedAt(now)
                .build();
        return rulesNoteRepository.save(note);
    }

    private RulesNoteResponse toResponse(RulesNote note) {
        return RulesNoteResponse.builder()
                .userId(note.getUserId())
                .tradingRules(note.getTradingRules())
                .disciplineRules(note.getDisciplineRules())
                .motivationNotes(note.getMotivationNotes())
                .dailyReminders(note.getDailyReminders())
                .build();
    }
}
