package com.ceylontrail.backend_server.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResetPasswordDTO {

    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "OTP is required!")
    private String otp;

    @NotBlank(message = "Password is required!")
    private String password;


}