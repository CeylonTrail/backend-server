package com.ceylontrail.backend_server.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventEntity {
    @Id
    @Column(name = "event_id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int eventId;

    @ManyToOne
    @JoinColumn(name="trip_id", nullable=false)
    private TripEntity trip;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private PlaceEntity place;

    @Column(name = "description", length = 100,nullable = false)
    private String description;

    @Column(name = "day_num",nullable = false)
    private int dayNum;

    @Column(name = "created_at",columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "update_at",columnDefinition = "TIMESTAMP")
    private LocalDateTime updateAt;

}
