package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.ChatMessageDto;
import com.ceylontrail.backend_server.service.ChatService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@CrossOrigin
@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatMessageDto message, Principal principal)
    {
        chatService.sendMessage(message, principal);
    }

    @GetMapping("/api/v1/chat/history/{receiver}")
    public StandardResponse getHistory(Principal principal,@PathVariable("receiver") String receiver)
    {
        return chatService.getHistory(principal, receiver);
    }
}
