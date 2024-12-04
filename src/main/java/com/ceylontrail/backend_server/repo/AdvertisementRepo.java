package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.AdvertisementEntity;
import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvertisementRepo extends JpaRepository<AdvertisementEntity,Long> {
    List<AdvertisementEntity> findByServiceProvider(ServiceProviderEntity sp);

    AdvertisementEntity findByAdvertisementId(Long advertisementId);

    @Query("SELECT a FROM AdvertisementEntity a WHERE a.isActive = 'YES' ORDER BY a.createdAt DESC")
    List<AdvertisementEntity> findAllActiveAdvertisements();
}
