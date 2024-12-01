package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<PaymentEntity, Long> {
}
