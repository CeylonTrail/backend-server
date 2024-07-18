package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserName(String userName);
    Boolean existsByUserName(String userName);
}
