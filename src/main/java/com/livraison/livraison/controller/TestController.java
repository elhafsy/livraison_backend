package com.livraison.livraison.controller;

import com.livraison.livraison.model.Notification;
import com.livraison.livraison.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public TestController(NotificationService notificationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.notificationService = notificationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/test-notification")
    public String sendTestNotification(@RequestParam String userId) {
        String destination = "/queue/notifications-" + userId;
        Notification notification = new Notification();
        notification.setContent("Test Notification");
        notification.setTitle("Test Notification");
        simpMessagingTemplate.convertAndSend(destination, notification);

        return "Notification envoyée à l'utilisateur ";
    }

}
