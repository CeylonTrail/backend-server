package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.AuthResponseDTO;
import com.ceylontrail.backend_server.dto.LoginDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;

import java.util.Map;

public interface AuthService {
    AuthResponseDTO register(RegisterDTO registerDTO);

    AuthResponseDTO login(LoginDTO loginDTO);
}