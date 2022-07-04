package com.example.itask.config;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

public class WebSocketConfig {

    @Order(HIGHEST_PRECEDENCE + 99)
    @Configuration
    @RequiredArgsConstructor
    @EnableWebSocketMessageBroker
    public static class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.enableSimpleBroker(WebSocketDefinition.WS_EVENT_ENDPOINT);
            registry.setUserDestinationPrefix(WebSocketDefinition.WS_EVENT_ENDPOINT);
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint(WebSocketDefinition.WS_ENDPOINT)
                    .setAllowedOriginPatterns("*")
                    .withSockJS();
        }
    }
}
