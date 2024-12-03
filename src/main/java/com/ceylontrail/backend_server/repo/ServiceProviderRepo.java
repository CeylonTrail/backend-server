package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ServiceProviderRepo extends JpaRepository<ServiceProviderEntity, Long> {
    ServiceProviderEntity findByUser(UserEntity user);

    ServiceProviderEntity findByServiceProviderId(Long spId);

    List<ServiceProviderEntity> findBySubscriptionExpiryDateBefore(LocalDate date);

    @Query("SELECT COUNT(sp) FROM ServiceProviderEntity sp WHERE sp.verificationStatus = :status")
    int countBusinessProfiles(VerificationStatusEnum status);

    @Query("SELECT COUNT(sp) FROM ServiceProviderEntity sp WHERE sp.verificationStatus = :status AND sp.verificationStatusUpdatedAt >= :startDate")
    int countRecentBusinessProfiles(VerificationStatusEnum status, LocalDate startDate);

    @Query("SELECT sp FROM ServiceProviderEntity sp WHERE sp.verificationStatus = :status ORDER BY sp.verificationStatusUpdatedAt DESC")
    List<ServiceProviderEntity> findLatestPending(VerificationStatusEnum status, Pageable pageable);

    @Query("SELECT sp FROM ServiceProviderEntity sp WHERE sp.verificationStatus = :status ORDER BY sp.verificationStatusUpdatedAt DESC")
    List<ServiceProviderEntity> findALLPending(VerificationStatusEnum status);

}
