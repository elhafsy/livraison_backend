package com.livraison.livraison.repository;

import com.livraison.livraison.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Récupérer la conversation entre deux utilisateurs
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2) OR (m.sender.id = :userId2 AND m.receiver.id = :userId1) ORDER BY m.sentAt ASC")
    List<Message> findConversationBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    // Récupérer tous les messages non lus pour un utilisateur
    List<Message> findByReceiverIdAndIsReadFalseOrderBySentAtDesc(Long receiverId);

    // Récupérer les conversations récentes d'un utilisateur (derniers messages)
    @Query("SELECT m FROM Message m WHERE m.id IN (SELECT MAX(m2.id) FROM Message m2 WHERE m2.sender.id = :userId OR m2.receiver.id = :userId GROUP BY CASE WHEN m2.sender.id = :userId THEN m2.receiver.id ELSE m2.sender.id END) ORDER BY m.sentAt DESC")
    List<Message> findRecentConversations(@Param("userId") Long userId);
}
