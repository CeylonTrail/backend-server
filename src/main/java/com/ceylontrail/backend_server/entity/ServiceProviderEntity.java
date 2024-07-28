package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_provider")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_provider_id")
    private Long serviceProviderId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceType serviceType;

    public enum ServiceType {
        A,
        B,
        C
    }
}
