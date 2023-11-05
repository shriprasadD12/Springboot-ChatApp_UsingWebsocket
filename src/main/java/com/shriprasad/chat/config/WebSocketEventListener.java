package com.shriprasad.chat.config;

import com.shriprasad.chat.enums.MessageType;
import com.shriprasad.chat.models.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handWebSocketDisconnectListener(SessionDisconnectEvent event)
    {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(event.getMessage());
        String userName =(String) headerAccessor.getSessionAttributes().get("userName");

        if(userName!= null){
            log.info("user disconnected:{}", userName);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(userName)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }

}
