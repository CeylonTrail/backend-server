package com.ceylontrail.backend_server.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoggedAdminDTO {

    private String accessToken;
    private String role = "ADMIN";
    private int userId;
    private String username;
    private String email;

}

