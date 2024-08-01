package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "trip")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class  TripEntity {
    @Id
    @Column(name = "trip_id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int tripId;

    @Column(name = "user_id",length = 100,nullable = false)
    private int userId;

    @Column(name = "destination",length = 100)
    private String destination;

    @Column(name = "day_count",length = 10,nullable = false)
    private int dayCount;

    @Column(name = "description",length = 255)
    private String description;

    @Column(name = "created_at",columnDefinition = "TIMESTAMP")
    private LocalDate createdAt;

    @Column(name = "update_at",columnDefinition = "TIMESTAMP")
    private LocalDate updateAt;

    @OneToMany(mappedBy="trip")
    private Set<EventEntity> eventSet;

    @OneToMany(mappedBy="trip")
    private Set<ExpenseEntity> expenseSet;


    public TripEntity(String destination, int dayCount, String description, LocalDate createdAt, LocalDate updateAt) {
        this.destination = destination;
        this.dayCount = dayCount;
        this.description = description;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }
}
