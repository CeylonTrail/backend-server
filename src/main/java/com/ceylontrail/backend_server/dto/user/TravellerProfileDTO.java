package com.ceylontrail.backend_server.dto.user;

import com.ceylontrail.backend_server.dto.post.GetPostFeedDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TravellerProfileDTO {

    private int userId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String profilePictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GetPostFeedDTO> posts = new ArrayList<>();
    private List<String> images = new ArrayList<>();

}