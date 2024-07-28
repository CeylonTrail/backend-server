package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseEntity {
    @Id
    @Column(name = "expense_id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int expenseId;

    @ManyToOne
    @JoinColumn(name="trip_id", nullable=false)
    private TripEntity trip;

    @Column(name = "category", length = 100,nullable = false)
    private String category;

    @Column(name = "description", length = 100,nullable = false)
    private String description;

    @Column(name = "amount",nullable = false)
    private double amount;

    @Column(name = "created_at",columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "update_at",columnDefinition = "TIMESTAMP")
    private LocalDateTime updateAt;
}
