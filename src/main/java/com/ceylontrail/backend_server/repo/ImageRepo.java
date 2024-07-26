package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<ImageEntity, String> {
    boolean existsByFilename(String filename);
    ImageEntity findByFilename(String filename);
}
