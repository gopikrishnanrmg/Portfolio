package com.notification.notification_service.configs;

import com.notification.notification_service.handlers.NotificationHandler;
import com.notification.notification_service.handlers.NotificationTelegramHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class NotificationHandlerConfig {

    @Bean
    @Primary
    public NotificationHandler NotificationHandlerChain(NotificationTelegramHandler telegramHandler) {
        return telegramHandler;
    }
}
