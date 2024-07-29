package com.ceylontrail.backend_server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommunityUserDTO {

    private int userId;
    private String username;
    private String profilePictureUrl;

}
