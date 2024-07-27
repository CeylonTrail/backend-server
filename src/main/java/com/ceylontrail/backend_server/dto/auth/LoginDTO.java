package com.ceylontrail.backend_server.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Password is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;

}
