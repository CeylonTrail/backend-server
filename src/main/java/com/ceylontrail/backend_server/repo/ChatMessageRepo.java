package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepo extends JpaRepository<ChatMessageEntity,Long> {
    List<ChatMessageEntity> findBySenderAndReceiver(String sender, String receiver);
}
