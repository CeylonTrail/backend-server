package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.ChatMessageDto;
import com.ceylontrail.backend_server.entity.ChatMessageEntity;
import com.ceylontrail.backend_server.repo.ChatMessageRepo;
import com.ceylontrail.backend_server.service.ChatService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceIMPL implements ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepo repo;

    public ChatServiceIMPL(SimpMessagingTemplate messagingTemplate,ChatMessageRepo repo) {
        this.messagingTemplate = messagingTemplate;
        this.repo = repo;
    }

    @Override
    public void sendMessage(ChatMessageDto message, Principal principal) {
        String sender = principal.getName();
        if (sender.equals(message.getSender())) {
            ChatMessageEntity entity = new ChatMessageEntity();
            entity.setSender(message.getSender());
            entity.setReceiver(message.getReceiver());
            entity.setContent(message.getContent());
            entity.setTimestamp(LocalDateTime.now());
            ChatMessageEntity result = repo.save(entity);
            messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/messages", result);

        } else {
            throw new IllegalArgumentException("Sender is not the same as principal");
        }
    }

    @Override
    public StandardResponse getHistory(Principal principal, String receiver) {
        String sender = principal.getName();
        List<ChatMessageEntity> list1 = repo.findBySenderAndReceiver(sender, receiver);
        List<ChatMessageEntity> list2 = repo.findBySenderAndReceiver(receiver, sender);

        list1.addAll(list2);

        return new StandardResponse(200, "Chat history fetched successfully", list1);
    }
}
