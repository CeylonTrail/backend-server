package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface UserService {
    UserEntity initialUserCheck(int userId);

    StandardResponse getUserProfile();
}
