package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.requests.CreateExpenseDTO;
import com.ceylontrail.backend_server.service.ExpenseService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceIMPL implements ExpenseService {
    @Override
    public StandardResponse saveExpense(CreateExpenseDTO createExpenseDTO) {
        return null;
    }

}
