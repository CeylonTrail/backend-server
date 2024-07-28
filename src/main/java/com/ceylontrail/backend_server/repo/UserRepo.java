package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    UserEntity findByUserId(int userId);
    UserEntity findByEmail(String email);
    UserEntity findByForgetPasswordOtp(String forgetPasswordOtp);
    UserEntity findByActivationToken(String activationToken);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    boolean existsByForgetPasswordOtp(String forgetPasswordOtp);
    boolean existsByActivationToken(String activationToken);
}
