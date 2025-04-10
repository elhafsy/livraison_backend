package com.livraison.livraison.service;

import com.livraison.livraison.dto.NotificationDTO;
import com.livraison.livraison.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Long userId, NotificationDTO notificationDTO) {

        messagingTemplate.convertAndSendToUser(userId.toString(),"/queue/notifications/", notificationDTO);
    }

    public void updateUserList(){
        messagingTemplate.convertAndSend("/topic/users","update");
    }
}
