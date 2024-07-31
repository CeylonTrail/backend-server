package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.auth.LoginDTO;
import com.ceylontrail.backend_server.dto.auth.*;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface AuthService {
    Integer getAuthUserId();
    void initialRegisterCheck(String email, String username);
    String activationTokenGenerator();
    String otpGenerator();
    UserEntity createUser(String email, String username, String password, String firstname, String lastname, String role);
    StandardResponse registerTraveller(TravellerRegisterDTO registerDTO);
    StandardResponse registerServiceProvider(ServiceProviderRegisterDTO registerDTO);
    StandardResponse login(LoginDTO loginDTO);
    StandardResponse activate(ActivationTokenDTO tokenDTO);
    StandardResponse forgetPassword(EmailDTO emailDTO);
    StandardResponse validateOtp(OtpDTO otpDTO);
    StandardResponse resetPassword(ResetPasswordDTO resetDTO);
}