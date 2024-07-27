package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderRepo extends JpaRepository<ServiceProviderEntity, Integer> {
}
