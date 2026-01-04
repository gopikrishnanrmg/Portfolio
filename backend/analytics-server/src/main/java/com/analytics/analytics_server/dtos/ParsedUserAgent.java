package com.analytics.analytics_server.dtos;

import lombok.Builder;

@Builder
public record ParsedUserAgent(String browser, String os, String device) {}
