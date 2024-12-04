package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepo extends JpaRepository<ImageEntity, String> {
    boolean existsByFilename(String filename);
    ImageEntity findByFilename(String filename);

    @Query("SELECT i.url FROM ImageEntity i JOIN i.post p WHERE p.user.userId = :userId")
    List<String> findAllImageUrlsByUserId( int userId);

    ImageEntity findByUrl(String url);
}
