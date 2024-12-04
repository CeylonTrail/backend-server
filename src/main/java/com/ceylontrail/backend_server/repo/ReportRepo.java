package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ReportRepo extends JpaRepository<ReportEntity, Long> {

    @Query("SELECT COUNT(r) FROM ReportEntity r WHERE r.status = 'PENDING'")
    int countReports();

    @Query("SELECT COUNT(r) FROM ReportEntity r WHERE r.status = 'PENDING' AND r.createdAt >= :startDate")
    int countRecentReports(LocalDateTime startDate);

}
