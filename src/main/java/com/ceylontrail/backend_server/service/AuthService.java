package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.dto.auth.EmailDTO;
import com.ceylontrail.backend_server.dto.auth.OtpDTO;
import com.ceylontrail.backend_server.dto.auth.ResetPasswordDTO;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface AuthService {
    String otpGenerator();
    StandardResponse register(RegisterDTO registerDTO);
    StandardResponse login(LoginDTO loginDTO);
    StandardResponse forgetPassword(EmailDTO emailDTO);
    StandardResponse validateOtp(OtpDTO otpDTO);
    StandardResponse resetPassword(ResetPasswordDTO resetDTO);
}