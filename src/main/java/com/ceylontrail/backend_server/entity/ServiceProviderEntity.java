package com.ceylontrail.backend_server.entity;

import com.ceylontrail.backend_server.entity.enums.ServiceProviderTypeEnum;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "is_setup_complete")
    private String isSetupComplete;

    @Column(name = "service_name")
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceProviderTypeEnum serviceType;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "cover_picture_url")
    private String coverPictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatusEnum verificationStatus;

    @Column(name = "verification_doc_url")
    private String verificationDocUrl;

    @ElementCollection
    @CollectionTable(name = "social_media_links", joinColumns = @JoinColumn(name = "service_provider_id"))
    private List<SocialMediaLinks> socialMediaLinks;

    @ElementCollection
    @CollectionTable(name = "opening_hours", joinColumns = @JoinColumn(name = "service_provider_id"))
    private List<OpeningHours> openingHours;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private SubscriptionPlanEntity subscriptionPlan;

    @Column(name = "subscription_purchase_date")
    private LocalDate subscriptionPurchaseDate;

    @Column(name = "subscription_expiry_date")
    private LocalDate subscriptionExpiryDate;

    @Column(name = "subscription_duration_in_days")
    private int subscriptionDurationInDays;

}
