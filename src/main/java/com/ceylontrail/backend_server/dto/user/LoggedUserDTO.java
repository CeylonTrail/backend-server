package com.ceylontrail.backend_server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoggedUserDTO {

    private String accessToken;
    private int userId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private boolean accountState;
    private String role; //ADMIN, TRAVELLER, SERVICE_PROVIDER
    private String profilePictureUrl;

}

