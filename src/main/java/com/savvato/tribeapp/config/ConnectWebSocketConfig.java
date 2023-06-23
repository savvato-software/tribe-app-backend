package com.savvato.tribeapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ConnectWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // broker will carry messages back to client based on link "/secured/user/queue/specific-user-userabcde123"
        config.enableSimpleBroker("/connect/user/queue/specific-user");
        // set prefix that prompts Spring to make a new queue specific to the user
        config.setUserDestinationPrefix("/connect/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // the endpoint which frontend will subscribe to
        registry.addEndpoint("/connect/room").withSockJS();
    }
}