package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;
import java.util.List;

@Entity
@Table(name = "trip")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "image_url",length = 255)
    private String imageURL;

    @CreatedDate
    @Column(name = "created_at",columnDefinition = "TIMESTAMP")
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "update_at",columnDefinition = "TIMESTAMP")
    private LocalDate updateAt;

    @OneToMany(mappedBy="trip",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> eventSet;

    public TripEntity(String destination, int dayCount, String description, LocalDate createdAt, LocalDate updateAt) {
        this.destination = destination;
        this.dayCount = dayCount;
        this.description = description;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }
}
