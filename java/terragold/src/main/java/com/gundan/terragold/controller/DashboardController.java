package com.gundan.terragold.controller;

import com.gundan.terragold.service.DashboardService;
import com.gundan.terragold.util.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ApiResponseBuilder api;

    @GetMapping("/today-production")
    public Object todayProduction() {
        return api.buildResponse(
                dashboardService.getTodayProduction(),
                null, null, null, null,
                List.of("Today's production fetched"),
                HttpStatus.OK
        );
    }

    @GetMapping("/monthly-production")
    public Object monthlyProduction() {
        return api.buildResponse(
                dashboardService.getCurrentMonthProduction(),
                null, null, null, null,
                List.of("Monthly production fetched"),
                HttpStatus.OK
        );
    }

    @GetMapping("/stats")
    public Object stats() {
        return api.buildResponse(
                dashboardService.getEmployeeAndPayrollStats(),
                null, null, null,null,
                List.of("Stats fetched"),
                HttpStatus.OK
        );
    }

    @GetMapping("/recent-activity")
    public Object recentActivity() {
        return api.buildResponse(
                dashboardService.getRecentActivity(6),
                null, null, null, null,
                List.of("Recent activity fetched"),
                HttpStatus.OK
        );
    }

    @GetMapping("/production-trend")
    public Object productionTrend() {
        return api.buildResponse(
                dashboardService.getLast7DaysTrend(),
                null, null, null, null,
                List.of("Production trend fetched"),
                HttpStatus.OK
        );
    }
}