package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<EventEntity,Integer> {
}
