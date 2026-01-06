package com.analytics.analytics_server.controllers;

import com.analytics.analytics_server.dtos.CreateAnalyticsRequest;
import com.analytics.analytics_server.services.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping()
    public ResponseEntity<Void> createAnalytics(@RequestBody CreateAnalyticsRequest req, HttpServletRequest request) {
        analyticsService.createAnalytics(req, request);
        return ResponseEntity.noContent().build();
    }
}
