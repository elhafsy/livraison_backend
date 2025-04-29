package com.livraison.livraison.controller;

import com.livraison.livraison.dto.ConversationSummaryDTO;
import com.livraison.livraison.dto.MessageDTO;
import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.dto.request.NewMessageRequest;
import com.livraison.livraison.security.services.UserDetailsImpl;
import com.livraison.livraison.service.ChatService;
import com.livraison.livraison.service.NotificationService;
import com.livraison.livraison.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationSummaryDTO>> getUserConversations(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(chatService.getUserConversation(userDetails.getId()));
    }

    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @PathVariable Long otherUserId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(chatService.getConversation(otherUserId, userDetails.getId()));
    }
    @GetMapping("/getUser/{idUser}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long idUser){
        if (userService.getUser(idUser) != null) {
            return ResponseEntity.ok(userService.getUser(idUser));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> sendMessage(
            @Valid @RequestBody NewMessageRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.sendMessage(request, userDetails.getId()));
    }
}
