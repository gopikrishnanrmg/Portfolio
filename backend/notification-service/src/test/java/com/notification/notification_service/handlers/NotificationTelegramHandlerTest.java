package com.notification.notification_service.handlers;

import com.notification.notification_service.dtos.Message;
import com.notification.notification_service.services.TelegramService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationTelegramHandlerTest {

    private TelegramService telegramService;
    private NotificationTelegramHandler handler;

    @BeforeEach
    void setup() {
        telegramService = mock(TelegramService.class);
        handler = new NotificationTelegramHandler(telegramService);
    }

    @Test
    void sendMessage_forwardsToNextHandler() {
        Message message = new Message("1", "title", "message", "email", 1234);
        NotificationHandler next = mock(NotificationHandler.class);
        handler.addHandler(next);
        handler.sendMessage(message);
        verify(next, times(1)).sendMessage(message);
    }

    @Test
    void sendMessage_callsTelegramService() {
        Message message = new Message("1", "title", "message", "email", 1234);
        handler.sendMessage(message);
        verify(telegramService, times(1)).sendMessage(message);
    }

    @Test
    void addHandler_returnsSameHandler() {
        NotificationHandler next = mock(NotificationHandler.class);
        NotificationHandler returned = handler.addHandler(next);
        assertSame(next, returned);
    }

}