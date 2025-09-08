package com.example.Project.Management.bayport.dashboard.controller;

import com.example.Project.Management.bayport.dashboard.dto.DashboardDto;
import com.example.Project.Management.bayport.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DatshboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/api/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DashboardDto> getDashboard() {
        DashboardDto dashboard = dashboardService.createDashboard();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DashboardDto> getStatistics() {
        DashboardDto statistics = dashboardService.statistics();
        return ResponseEntity.ok(statistics);
    }

}
