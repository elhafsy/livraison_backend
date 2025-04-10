package com.livraison.livraison.controller;

import com.livraison.livraison.dto.NotificationDTO;
import com.livraison.livraison.model.Notification;
import com.livraison.livraison.model.User;
import com.livraison.livraison.security.services.UserDetailsImpl;
import com.livraison.livraison.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userDetails.getId()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Notification notification = notificationService.markAsRead(id, userDetails.getId());
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        int modifiedCount = notificationService.markAllAsRead(userDetails.getId());
        return ResponseEntity.ok("Toutes les notifications ont été marquées comme lues. Nombre de notifications modifiées : " + modifiedCount);
    }
}
