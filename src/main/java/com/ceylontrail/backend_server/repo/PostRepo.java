package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<PostEntity, Long> {
    PostEntity findByPostId(Long postId);
}

