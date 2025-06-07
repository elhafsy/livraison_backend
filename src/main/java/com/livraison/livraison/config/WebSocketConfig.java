package com.livraison.livraison.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Permet les notifications globales (tous les utilisateurs) via "/topic"
        config.enableSimpleBroker("/topic", "/queue");
        // Définit le préfixe pour les messages envoyés au serveur depuis le client
        config.setApplicationDestinationPrefixes("/app");
        //Active la gestion des destinations privées pour chaque utilisateur
       // config.setUserDestinationPrefix("/user");


    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configure l'endpoint WebSocket avec SockJS fallback
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://192.168.1.167:3000") // URL de votre frontend React
                .withSockJS();
    }
}
