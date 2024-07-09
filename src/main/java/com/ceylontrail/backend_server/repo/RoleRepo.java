package com.ceylontrail.backend_server.repo;
import com.ceylontrail.backend_server.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity,Integer> {
    Optional<RoleEntity> findByName(String name);
}

