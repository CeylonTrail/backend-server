package com.ceylontrail.backend_server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private String email;
    private String userName;

    @NotBlank(message = "Password is required!")
    private String password;
}
