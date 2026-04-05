package com.disciplineledger.controller;

import com.disciplineledger.dto.DashboardResponse;
import com.disciplineledger.security.SecurityUtil;
import com.disciplineledger.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard(SecurityUtil.getCurrentUserId());
    }
}
