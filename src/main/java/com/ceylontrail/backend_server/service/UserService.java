package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.entity.UserEntity;

public interface UserService {
    UserEntity initialUserCheck(int userId);
}
