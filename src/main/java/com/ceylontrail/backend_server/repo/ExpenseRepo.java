package com.ceylontrail.backend_server.repo;

import com.ceylontrail.backend_server.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepo extends JpaRepository<ExpenseEntity,Integer> {
}
