package com.ceylontrail.backend_server.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ActivationTokenDTO {

    @NotBlank(message = "Activation Token is required!")
    private String activationToken;

}
