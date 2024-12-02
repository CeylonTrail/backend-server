package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepo extends JpaRepository<PaymentEntity, Long> {

    @Query("SELECT SUM(p.amount) FROM PaymentEntity p")
    Double calculateTotalRevenue();

    @Query("SELECT SUM(p.amount) FROM PaymentEntity p WHERE p.date >= :startDate")
    Double calculateRevenueForRecentDays(LocalDate startDate);

    @Query("""
       SELECT YEAR(p.date) AS year, MONTH(p.date) AS month, COUNT(p) AS count\s
       FROM PaymentEntity p\s
       WHERE p.type = 'SUBSCRIPTION'\s
       AND p.date >= :startDate\s
       GROUP BY YEAR(p.date), MONTH(p.date)\s
       ORDER BY YEAR(p.date), MONTH(p.date)
      \s""")
    List<Object[]> countSubscriptionsByMonth(LocalDate startDate);
}
