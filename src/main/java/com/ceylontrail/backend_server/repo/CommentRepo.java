package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {
    CommentEntity findByCommentId(Long commentId);
}

