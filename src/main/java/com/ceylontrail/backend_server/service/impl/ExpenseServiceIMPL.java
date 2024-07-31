package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.requests.CreateExpenseDTO;
import com.ceylontrail.backend_server.entity.ExpenseEntity;
import com.ceylontrail.backend_server.entity.TravellerEntity;
import com.ceylontrail.backend_server.entity.TripEntity;
import com.ceylontrail.backend_server.repo.ExpenseRepo;
import com.ceylontrail.backend_server.repo.TravellerRepo;
import com.ceylontrail.backend_server.repo.TripRepo;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.ExpenseService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpenseServiceIMPL implements ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private TripRepo tripRepo;

    @Autowired
    private AuthService authService;

    @Autowired
    private TravellerRepo travelerRepo;

    @Override
    public StandardResponse saveExpense(CreateExpenseDTO createExpenseDTO) {
        int userId = authService.getAuthUserId();
        Optional<TravellerEntity> traveller = travelerRepo.findById(userId);
        Optional<TripEntity> trip = tripRepo.findById(createExpenseDTO.getTripId());

        if (traveller.isPresent() && trip.isPresent()){
            if (trip.get().getTraveller().getTravellerId() == traveller.get().getTravellerId()) {
                ExpenseEntity expense = new ExpenseEntity();
                expense.setCategory(createExpenseDTO.getCategory());
                expense.setAmount(createExpenseDTO.getAmount());
                expense.setCreatedAt(createExpenseDTO.getCreatedAt());
                expense.setUpdateAt(createExpenseDTO.getUpdatedAt());
                expense.setTraveller(traveller.get());
                expense.setTrip(trip.get());

                expenseRepo.save(expense);

                return new StandardResponse(200, "Expense saved", createExpenseDTO);
            } else {
                return new StandardResponse(403, "Trip does not belong to the traveler", createExpenseDTO);
            }
        } else{
            if (!traveller.isPresent()) {
                return new StandardResponse(404, "Traveler not found", createExpenseDTO);
            } else {
                return new StandardResponse(404, "Trip not found", createExpenseDTO);
            }
        }

    }


}
