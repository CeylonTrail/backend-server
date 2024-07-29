package com.ceylontrail.backend_server.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceProviderRegisterDTO {

    @NotBlank(message = "Username is required!")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required!")
    @Email(message = "Valid email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Firstname is required!")
    private String firstname;

    private String lastname;

    @NotBlank(message = "Service name is required!")
    private String serviceName;

    @NotNull(message = "Service type is required!")
    private String serviceType;

    @NotNull(message = "Latitude is required!")
    private Double latitude;

    @NotNull(message = "Longitude is required!")
    private Double longitude;

}
