package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.requests.CreateExpenseDTO;
import com.ceylontrail.backend_server.service.ExpenseService;
import com.ceylontrail.backend_server.service.TripService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/traveller")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping ("/add-expense")
    public StandardResponse saveExpense(@RequestBody CreateExpenseDTO createExpenseDTO) {
        return expenseService.saveExpense(createExpenseDTO);
    }
}
