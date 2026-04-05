package com.disciplineledger.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RulesNoteRequest {
    private String tradingRules;
    private String disciplineRules;
    private String motivationNotes;
    private String dailyReminders;
}
