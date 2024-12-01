package com.ceylontrail.backend_server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoggedTravellerDTO {

    private String accessToken;
    private String role = "TRAVELLER";
    private int userId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private boolean accountState;
    private String profilePictureUrl;

}

