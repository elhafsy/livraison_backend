package com.livraison.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSummaryDTO {
    private UserDTO otherUser;
    private MessageDTO lastMessage;
    private int unreadCount;
}
