package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceIMPL implements UserService {

    private final UserRepo userRepo;

    @Override
    public UserEntity initialUserCheck(int userId) {
        UserEntity user = userRepo.findByUserId(userId);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }
        return user;
    }

}
