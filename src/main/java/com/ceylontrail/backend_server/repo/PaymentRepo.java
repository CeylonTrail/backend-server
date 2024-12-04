package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.PaymentEntity;
import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import org.springframework.data.domain.Pageable;
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
        SELECT EXTRACT(YEAR FROM p.date) AS year, EXTRACT(MONTH FROM p.date) AS month, COUNT(p) AS count
        FROM PaymentEntity p
        WHERE p.type = 'SUBSCRIPTION' AND p.date >= :startDate
        GROUP BY EXTRACT(YEAR FROM p.date), EXTRACT(MONTH FROM p.date)
        ORDER BY EXTRACT(YEAR FROM p.date), EXTRACT(MONTH FROM p.date)
        """)
    List<Object[]> countSubscriptionsByMonth(LocalDate startDate);

    @Query("""
        SELECT sp
        FROM ServiceProviderEntity sp
        WHERE sp.serviceProviderId IN (
            SELECT p.serviceProvider.serviceProviderId
            FROM PaymentEntity p
            ORDER BY p.date DESC
            )
        """)
    List<ServiceProviderEntity> findLatestSubscribers(Pageable pageable);



}
