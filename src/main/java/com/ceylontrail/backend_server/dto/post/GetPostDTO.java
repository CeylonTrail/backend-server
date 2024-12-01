package com.ceylontrail.backend_server.dto.post;

import com.ceylontrail.backend_server.dto.trip.CommunityTripDTO;
import com.ceylontrail.backend_server.dto.user.CommunityUserDTO;
import com.ceylontrail.backend_server.entity.enums.PostPrivacyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetPostDTO {

    private Long postId;
    private CommunityUserDTO user;
    private String content;
    private CommunityTripDTO trip;
    private PostPrivacyEnum privacy;
    private List<CommunityUserDTO> likes = new ArrayList<>();
    private List<GetCommentDTO> comments = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
