package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;

public interface AuthService {
    String register(RegisterDTO registerDto);

    String login(LoginDTO loginDTO);
}