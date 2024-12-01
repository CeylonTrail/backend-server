package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.ChatMessageDto;
import com.ceylontrail.backend_server.util.StandardResponse;

import java.security.Principal;

public interface ChatService {
    void sendMessage(ChatMessageDto message, Principal principal);
    StandardResponse getHistory(Principal principal,String receiver);
}
