package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "advertisement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id", nullable = false, updatable = false)
    private Long advertisementId;

    @Column(name = "title")
    private String title;

    @Column(name = "user_id")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "service_provider_id", nullable = false)
    private ServiceProviderEntity serviceProvider;

    @Column(name = "description")
    private String description;

    @Column(name = "ratetype")
    private String rateType;

    @Column(name = "rate")
    private double rate;

    @Column(name = "discount")
    private double discount;

//
//    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL)
//    private List<ImageEntity> images;

    public AdvertisementEntity(String title, String description, String rateType, double rate, double discount) {
        this.title = title;
        this.description = description;
        this.rateType = rateType;
        this.rate = rate;
        this.discount = discount;
    }
}
