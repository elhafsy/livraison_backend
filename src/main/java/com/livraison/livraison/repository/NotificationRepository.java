package com.livraison.livraison.repository;

import com.livraison.livraison.model.Notification;
import com.livraison.livraison.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndIsReadFalse(Long userId);

    List<Notification> findByUserId(Long userId);

    Optional<Notification> findByIdAndUserId(Long id, Long userId);

    //List<Notification> findByUserIdAndIsReadFalse(Long userId);


}
