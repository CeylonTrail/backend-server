package com.ceylontrail.backend_server.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OtpDTO {

    @NotBlank(message = "OTP is required!")
    private String otp;

}
