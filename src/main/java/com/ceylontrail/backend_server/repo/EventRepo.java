package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepo extends JpaRepository<EventEntity,Integer> {


    List<EventEntity> findAllByTrip_TripId(int tripId);

}
