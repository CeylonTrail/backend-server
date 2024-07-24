package com.ceylontrail.backend_server.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Long commentId;
    private Long postId;
    private int userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
