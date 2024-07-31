package com.ceylontrail.backend_server.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceProviderRegisterDTO {

    @NotBlank(message = "Service name is required!")
    private String serviceName;

    @NotNull(message = "Service type is required!")
    private String serviceType;

    @NotBlank(message = "Email is required!")
    @Email(message = "Valid email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Username is required!")
    @Size(min = 1, max = 32, message = "Username must be between 1 and 32 characters")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "Username can only contain lowercase letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Firstname is required!")
    private String firstname;

    private String lastname;

}
