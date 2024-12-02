package com.ceylontrail.backend_server.dto.post;

import com.ceylontrail.backend_server.dto.user.CommunityUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentDTO {

    private Long commentId;
    private CommunityUserDTO user;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
