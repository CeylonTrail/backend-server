package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.MarketPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketPlaceRepo extends JpaRepository<MarketPlaceEntity,Long> {
}
