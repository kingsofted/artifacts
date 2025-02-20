package com.example.hogwarts.hogwarts.webSocket;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;




@Configuration
// @EnableWebSocket
public class WebSocketConfig{
    // @Override
    // public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    //     registry.addHandler(new MyWebSocketHandler(), "/ws/{sid}").setAllowedOrigins("*");
    // }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
