package com.disciplineledger.controller;

import com.disciplineledger.dto.RulesNoteRequest;
import com.disciplineledger.dto.RulesNoteResponse;
import com.disciplineledger.security.SecurityUtil;
import com.disciplineledger.service.RulesNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RulesNoteController {

    private final RulesNoteService rulesNoteService;

    @GetMapping
    public RulesNoteResponse getRules() {
        return rulesNoteService.getRulesByUserId(SecurityUtil.getCurrentUserId());
    }

    @PutMapping
    public RulesNoteResponse updateRules(@RequestBody RulesNoteRequest request) {
        return rulesNoteService.upsertRules(SecurityUtil.getCurrentUserId(), request);
    }
}
