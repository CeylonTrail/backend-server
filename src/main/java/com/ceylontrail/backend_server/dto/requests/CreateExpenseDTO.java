package com.ceylontrail.backend_server.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateExpenseDTO {
    private int tripId;
    private int travellerId;
    private String category;
    @NotNull(message = "Amount is mandatory")
    private double amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
