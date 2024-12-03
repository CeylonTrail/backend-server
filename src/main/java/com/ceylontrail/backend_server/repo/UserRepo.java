package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
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

    @Query("SELECT i FROM UserEntity i WHERE i.isTraveller = 'YES'")
    List<UserEntity> getALLTravellers();

    @Query("SELECT COUNT(u) FROM UserEntity u")
    int countUsers();

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.isTraveller = 'YES'")
    int countTravellers();

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.createdAt >= :startDate")
    int countRecentUsers(LocalDateTime startDate);
}
