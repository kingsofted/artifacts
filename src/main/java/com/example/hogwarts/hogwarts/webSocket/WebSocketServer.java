package com.example.hogwarts.hogwarts.webSocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{sid}") // WebSocket path with session ID as a parameter
public class WebSocketServer {

    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        sessions.put(sid, session);
        System.out.println("WebSocket opened for session: " + sid);
        sendMessage(session, "Welcome! Your session ID is: " + sid);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("Received message from " + sid + ": " + message);
        // broadcast("User " + sid + ": " + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid) {
        sessions.remove(sid);
        System.out.println("WebSocket closed for session: " + sid);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket error: " + error.getMessage());
    }

    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        sessions.values().forEach(session -> sendMessage(session, message));
    }

    // Method to send a message to a specific user
    public static void sendMessageToUser(String userId, String message) {
        Session session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User not connected: " + userId);
        }
    }
    
}
