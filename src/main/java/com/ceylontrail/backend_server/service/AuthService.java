package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface AuthService {
    StandardResponse register(RegisterDTO registerDTO);

    StandardResponse login(LoginDTO loginDTO);
}