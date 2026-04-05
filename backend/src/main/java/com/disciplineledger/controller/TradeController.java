package com.disciplineledger.controller;

import com.disciplineledger.dto.TradeRequest;
import com.disciplineledger.dto.TradeResponse;
import com.disciplineledger.security.SecurityUtil;
import com.disciplineledger.service.TradeService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TradeResponse createTrade(@Valid @RequestBody TradeRequest request) {
        return tradeService.createTrade(SecurityUtil.getCurrentUserId(), request);
    }

    @GetMapping
    public List<TradeResponse> getTrades(@RequestParam(required = false) Long journalEntryId) {
        return tradeService.getTrades(SecurityUtil.getCurrentUserId(), journalEntryId);
    }

    @GetMapping("/{id}")
    public TradeResponse getTrade(@PathVariable Long id) {
        return tradeService.getTradeById(SecurityUtil.getCurrentUserId(), id);
    }

    @PutMapping("/{id}")
    public TradeResponse updateTrade(@PathVariable Long id, @Valid @RequestBody TradeRequest request) {
        return tradeService.updateTrade(SecurityUtil.getCurrentUserId(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrade(@PathVariable Long id) {
        tradeService.deleteTrade(SecurityUtil.getCurrentUserId(), id);
    }
}
