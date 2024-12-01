package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.auth.LoginDTO;
import com.ceylontrail.backend_server.dto.auth.*;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface AuthService {
    Integer getAuthUserId();

    StandardResponse registerTraveller(TravellerRegisterDTO registerDTO);

    StandardResponse registerServiceProvider(ServiceProviderRegisterDTO registerDTO);

    StandardResponse login(LoginDTO loginDTO);

    StandardResponse activate(ActivationTokenDTO tokenDTO);

    StandardResponse forgetPassword(EmailDTO emailDTO);

    StandardResponse validateOtp(OtpDTO otpDTO);

    StandardResponse resetPassword(ResetPasswordDTO resetDTO);
}