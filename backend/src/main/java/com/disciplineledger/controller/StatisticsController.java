package com.disciplineledger.controller;

import com.disciplineledger.dto.StatisticsResponse;
import com.disciplineledger.security.SecurityUtil;
import com.disciplineledger.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public StatisticsResponse getStatistics() {
        return statisticsService.getStatistics(SecurityUtil.getCurrentUserId());
    }
}
