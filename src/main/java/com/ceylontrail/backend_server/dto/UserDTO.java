package com.ceylontrail.backend_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

}
