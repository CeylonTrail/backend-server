package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.SavedTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedTripRepo extends JpaRepository<SavedTripEntity,Integer> {
    List<SavedTripEntity> findAllByUserId(int userId);
}
