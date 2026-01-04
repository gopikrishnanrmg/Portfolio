package com.analytics.analytics_server.services;

import com.analytics.analytics_server.dtos.ParsedUserAgent;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Service;

@Service
public class UserAgentService {

    private final UserAgentAnalyzer analyzer =
            UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10000).build();

    public ParsedUserAgent parse(String userAgent) {
        UserAgent ua = analyzer.parse(userAgent);

        return ParsedUserAgent.builder()
                .browser(ua.getValue("AgentName"))
                .os(ua.getValue("OperatingSystemName"))
                .device(ua.getValue("DeviceName"))
                .build();
    }
}
