package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.AdvertisementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepo extends JpaRepository<AdvertisementEntity,Long> {
    List<AdvertisementEntity> findAllByUserId(int userId);

    boolean existsByUserId(int userId);
}
