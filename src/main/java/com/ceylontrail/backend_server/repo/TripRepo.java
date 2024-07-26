package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.RoleEntity;
import com.ceylontrail.backend_server.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepo extends JpaRepository<TripEntity,Integer> {
}
