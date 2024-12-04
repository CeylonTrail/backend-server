package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "subscription_plan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id", nullable = false, updatable = false)
    private Long subscriptionId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "ad_count")
    private int adCount;

    @ElementCollection
    @CollectionTable(name = "subscription_feature", joinColumns = @JoinColumn(name = "subscription_id"))
    private List<String> feature;

}
