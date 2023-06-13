package com.example.chatapp.config;

import com.example.chatapp.enums.MessageType;
import com.example.chatapp.payloads.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("user disconnected: {}", username);
            var chatMessage = ChatMessage.builder()
                    .content("Bye Bye")
                    .sender(username)
                    .type(MessageType.LEAVE)
                    .build();
            messageTemplate.convertAndSend("/topic/public-chat", chatMessage);
        }
    }

}
