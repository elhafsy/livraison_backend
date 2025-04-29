package com.livraison.livraison.service;

import com.livraison.livraison.dto.ConversationSummaryDTO;
import com.livraison.livraison.dto.MessageDTO;
import com.livraison.livraison.dto.request.NewMessageRequest;
import com.livraison.livraison.mapper.MessageMapper;
import com.livraison.livraison.mapper.UserMapper;
import com.livraison.livraison.model.Message;
import com.livraison.livraison.model.User;
import com.livraison.livraison.repository.MessageRepository;
import com.livraison.livraison.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final WebSocketNotificationService webSocketNotificationService;

    public MessageDTO sendMessage(NewMessageRequest request, Long senderId) {
        User sender = userRepository.findById(senderId).orElseThrow((()->new RuntimeException("Sender not found")));
        User receiver = userRepository.findById(request.getReceiverId()).orElseThrow((()->new RuntimeException("Receiver not found")));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message = messageRepository.save(message);
        MessageDTO messageDTO = MessageMapper.INSTANCE.messageToMessageDTO(message);
        webSocketNotificationService.notifyNewMessage(messageDTO);
        return messageDTO;
    }

    public List<MessageDTO> getConversation(Long userId1, Long userId2) {
        userRepository.findById(userId1).orElseThrow((()->new RuntimeException("User 1 not found")));
        userRepository.findById(userId2).orElseThrow((()->new RuntimeException("User 2 not found")));

        List<Message> messages = messageRepository.findConversationBetweenUsers(userId1, userId2);

        for (Message message : messages) {
            if (!message.isRead() && message.getReceiver().getId().equals(userId1)) {
                message.setRead(true);
                message.setReadAt(LocalDateTime.now());
                messageRepository.save(message);
            }
        }
        return messages.stream().map(MessageMapper.INSTANCE::messageToMessageDTO).collect(Collectors.toList());
    }

    public List<ConversationSummaryDTO> getUserConversation(Long userId){
        List<Message> recentMessages = messageRepository.findRecentConversations(userId);

        Map<Long, ConversationSummaryDTO> conversationMap = new HashMap<>();

        for (Message message : recentMessages) {
            // Déterminer l'autre utilisateur dans la conversation
            User currentUser = userRepository.getById(userId);
            User otherUser = message.getSender().getId().equals(userId)
                    ? message.getReceiver()
                    : message.getSender();

            // Compter les messages non lus
            List<Message> unreadMessages = messageRepository.findByReceiverIdAndIsReadFalseOrderBySentAtDesc(userId)
                    .stream()
                    .filter(m -> m.getSender().getId().equals(otherUser.getId()))
                    .collect(Collectors.toList());

            ConversationSummaryDTO summary = new ConversationSummaryDTO();
            summary.setOtherUser(UserMapper.INSTANCE.userToUserDTO(otherUser));
            summary.setLastMessage(MessageMapper.INSTANCE.messageToMessageDTO(message));
            summary.setUnreadCount(unreadMessages.size());

            conversationMap.put(otherUser.getId(), summary);
        }

        return new ArrayList<>(conversationMap.values());
    }
}
