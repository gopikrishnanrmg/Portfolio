package com.notification.notification_service.integration;

import com.notification.notification_service.dtos.Message;
import com.notification.notification_service.handlers.NotificationHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {
        RabbitAutoConfiguration.class
})
public class NotificationIntegrationTest {

    @Autowired
    private NotificationHandler notificationHandler;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void shouldSendMessage() {
        Message message = new Message("session_id", "title", "message", "email", 1234);
        notificationHandler.sendMessage(message);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(String.class));
    }
}
