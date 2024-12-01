package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<ReportEntity, Long> {
}
