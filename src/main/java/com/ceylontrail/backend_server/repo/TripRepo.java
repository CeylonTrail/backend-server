package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepo extends JpaRepository<TripEntity,Integer> {
    List<TripEntity> findAllByUserId(int userId);
    TripEntity findByTripId(int id);
}
