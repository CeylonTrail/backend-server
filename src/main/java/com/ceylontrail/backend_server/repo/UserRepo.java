package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("SELECT i FROM UserEntity i WHERE " +
            "i.isTraveller = 'YES' AND " +
            "(:key IS NULL OR i.firstname LIKE %:key% OR i.lastname LIKE %:key% OR " +
            "i.username LIKE %:key% OR i.email LIKE %:key%)")
    Page<UserEntity> searchTravellers(String key, Pageable pageable);

}
