package com.ceylontrail.backend_server.dto.post;

import com.ceylontrail.backend_server.dto.comment.GetCommentDTO;
import com.ceylontrail.backend_server.dto.user.CommunityUserDTO;
import com.ceylontrail.backend_server.entity.enums.PostPrivacyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetPostDTO {

    private Long postId;
    private CommunityUserDTO user;
    private String content;
    private PostPrivacyEnum privacy;
    private List<CommunityUserDTO> likes;
    private List<GetCommentDTO> comments;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
