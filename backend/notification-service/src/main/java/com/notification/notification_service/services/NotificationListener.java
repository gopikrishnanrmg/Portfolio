package com.notification.notification_service.services;

import com.notification.notification_service.dtos.Message;
import com.notification.notification_service.handlers.NotificationHandler;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationListener {

    private NotificationHandler notificationHandler;

    @RabbitListener(queues = "${rabbit.queue}")
    public void handleMessage(Message message) {
        notificationHandler.sendMessage(message);
    }

}

