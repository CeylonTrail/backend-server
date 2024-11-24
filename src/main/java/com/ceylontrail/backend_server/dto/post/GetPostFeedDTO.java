package com.ceylontrail.backend_server.dto.post;

import com.ceylontrail.backend_server.dto.trip.CommunityTripDTO;
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
public class GetPostFeedDTO {

    private Long postId;
    private CommunityUserDTO user;
    private String content;
    private CommunityTripDTO trip;
    private PostPrivacyEnum privacy;
    private int likeCount;
    private int commentCount;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
