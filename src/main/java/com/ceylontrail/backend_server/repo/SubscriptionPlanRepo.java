package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.SubscriptionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepo extends JpaRepository<SubscriptionPlanEntity, Long> {
    SubscriptionPlanEntity findBySubscriptionId(Long subscriptionId);
}
