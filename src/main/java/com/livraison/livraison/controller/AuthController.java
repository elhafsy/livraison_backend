package com.livraison.livraison.controller;

import com.livraison.livraison.dto.NotificationDTO;
import com.livraison.livraison.dto.UserDTO;
import com.livraison.livraison.dto.request.LoginRequest;
import com.livraison.livraison.dto.request.RegisterRequest;
import com.livraison.livraison.model.Notification;
import com.livraison.livraison.model.User;
import com.livraison.livraison.model.enums.Role;
import com.livraison.livraison.repository.UserRepository;
import com.livraison.livraison.security.jwt.JwtAuthFilter;
import com.livraison.livraison.security.services.UserDetailsImpl;
import com.livraison.livraison.service.AuthService;
import com.livraison.livraison.service.NotificationService;
import com.livraison.livraison.service.WebSocketNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final WebSocketNotificationService webSocketNotificationService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        }

    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getAuthenticatedUser(){
        UserDTO userDTO = authService.getAuthenticatedUser();
        if(userDTO != null){
            return ResponseEntity.ok(userDTO);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String message = authService.registerUser(request);
        if(message.equals("Email already in use")){
            return ResponseEntity.badRequest().body(message);
        }

        List<User> admins = userRepository.getUsersByRole(Role.ADMIN);
        for (User admin : admins) {
            notificationService.createNotification(
                    admin.getId(),
                    "Client Registred",
                    "Un nouveau client s'est inscrit"
            );
        }
        //NotificationDTO notificationDTO= notificationService.createNotification(1L,"Client Registered","new client inscrer");
        webSocketNotificationService.updateUserList();
        return ResponseEntity.ok().body("{\"message\": \"" + message + "\"}");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token,@AuthenticationPrincipal UserDetailsImpl userDetails ) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            JwtAuthFilter.invalidateToken(token);
            Optional<User> userOptional = userRepository.findById(userDetails.getId());
            if(userOptional.isPresent()){
                User user = userOptional.get();
                user.setOnline(false);
                userRepository.save(user);
            }
            return ResponseEntity.ok(Map.of("message", "logged out successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("message","Invalid token"));
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
