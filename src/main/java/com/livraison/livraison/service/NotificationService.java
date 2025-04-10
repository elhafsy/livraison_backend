package com.livraison.livraison.service;

import com.livraison.livraison.dto.NotificationDTO;
import com.livraison.livraison.mapper.NotificationMapper;
import com.livraison.livraison.model.Notification;
import com.livraison.livraison.model.User;
import com.livraison.livraison.repository.NotificationRepository;
import com.livraison.livraison.repository.UserRepository;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public NotificationDTO createNotification(Long userId,String title, String content) {
        Notification notification = new Notification();
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            notification.setUser(user.get());
            notification.setTitle(title);
            notification.setContent(content);
            Notification notificationSaved = notificationRepository.save(notification);
            NotificationDTO notificationDTO = NotificationMapper.INSTANCE.notificationToNotificationDTO(notificationSaved);
//            webSocketNotificationService.sendNotification(userId,notificationDTO);

            String destination = "/queue/notifications-" + userId;
            simpMessagingTemplate.convertAndSend(destination, notificationDTO);
            return notificationDTO;
        }
        return null;
    }

    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        List<Notification> notifications =  notificationRepository.findByUserIdAndIsReadFalse(userId);
        return notifications.stream().map(notificationMapper::notificationToNotificationDTO).collect(Collectors.toList());
    }

    public Notification markAsRead(Long id, Long userId) {
        Notification notification = notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée ou non autorisée."));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }
    public int markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
        return notifications.size();
    }



}
