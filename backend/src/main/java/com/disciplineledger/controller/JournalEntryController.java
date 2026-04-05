package com.disciplineledger.controller;

import com.disciplineledger.dto.JournalEntryRequest;
import com.disciplineledger.dto.JournalEntryResponse;
import com.disciplineledger.security.SecurityUtil;
import com.disciplineledger.service.JournalEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/journal-entries")
@RequiredArgsConstructor
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JournalEntryResponse createEntry(@Valid @RequestBody JournalEntryRequest request) {
        return journalEntryService.createEntry(SecurityUtil.getCurrentUserId(), request);
    }

    @GetMapping
    public List<JournalEntryResponse> getEntriesByUser() {
        return journalEntryService.getEntriesByUser(SecurityUtil.getCurrentUserId());
    }

    @GetMapping("/{id}")
    public JournalEntryResponse getEntry(@PathVariable Long id) {
        return journalEntryService.getEntryById(SecurityUtil.getCurrentUserId(), id);
    }

    @PutMapping("/{id}")
    public JournalEntryResponse updateEntry(@PathVariable Long id, @Valid @RequestBody JournalEntryRequest request) {
        return journalEntryService.updateEntry(SecurityUtil.getCurrentUserId(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable Long id) {
        journalEntryService.deleteEntry(SecurityUtil.getCurrentUserId(), id);
    }
}
