package com.ceylontrail.backend_server.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class ChatMessageDto {

    private String sender;
    private String receiver;
    private String content;
}
