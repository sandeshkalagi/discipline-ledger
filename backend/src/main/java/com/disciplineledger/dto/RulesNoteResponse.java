package com.disciplineledger.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RulesNoteResponse {
    private Long userId;
    private String tradingRules;
    private String disciplineRules;
    private String motivationNotes;
    private String dailyReminders;
}
