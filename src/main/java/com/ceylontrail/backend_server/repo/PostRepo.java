package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.entity.enums.PostPrivacyEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepo extends JpaRepository<PostEntity, Long> {
    PostEntity findByPostId(Long postId);
    List<PostEntity> findPostEntitiesByPrivacyInOrderByUpdatedAtDesc(Collection<PostPrivacyEnum> privacy);
    List<PostEntity> findPostEntitiesByUser_UserIdOrderByCreatedAtDesc(int userId);
}
