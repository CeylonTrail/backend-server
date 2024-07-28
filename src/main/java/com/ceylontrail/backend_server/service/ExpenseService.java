package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.requests.CreateExpenseDTO;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.stereotype.Service;

public interface ExpenseService {
    StandardResponse saveExpense(CreateExpenseDTO createExpenseDTO);
}
