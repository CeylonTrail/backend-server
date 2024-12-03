package com.ceylontrail.backend_server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Traveller {

    private int userId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private boolean accountStatus;
    private String createdAt;

}
