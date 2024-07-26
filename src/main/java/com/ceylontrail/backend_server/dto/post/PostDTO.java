package com.ceylontrail.backend_server.dto.post;

import com.ceylontrail.backend_server.dto.comment.CommentDTO;
import com.ceylontrail.backend_server.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {

    private Long postId;
    private int userId;
    private int tripId;
    private String content;
    private List<UserDTO> likes;
    private List<CommentDTO> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
