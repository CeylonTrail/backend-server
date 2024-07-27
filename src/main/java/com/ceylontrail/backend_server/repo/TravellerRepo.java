package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.TravellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravellerRepo extends JpaRepository<TravellerEntity, Integer> {
}
