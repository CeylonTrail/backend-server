package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.EventEntity;
import com.ceylontrail.backend_server.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepo extends JpaRepository<PlaceEntity,Long> {

    boolean existsByPlaceId(String placeId);
    PlaceEntity findByPlaceId(String placeId);
}
