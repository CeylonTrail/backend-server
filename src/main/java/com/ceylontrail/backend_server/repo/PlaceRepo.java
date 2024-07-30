package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.EventEntity;
import com.ceylontrail.backend_server.entity.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepo extends JpaRepository<PlaceEntity,Long> {
}
