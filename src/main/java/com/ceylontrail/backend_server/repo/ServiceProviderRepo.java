package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceProviderRepo extends JpaRepository<ServiceProviderEntity, Integer> {

    @Query("SELECT sp FROM ServiceProviderEntity sp " +
            "JOIN sp.user u " +
            "WHERE (:key IS NULL OR " +
            "LOWER(sp.serviceName) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(sp.serviceType) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :key, '%'))) ")
    Page<ServiceProviderEntity> searchSPs(String key, Pageable pageable);

}
