package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Map;

@Entity
@Table(name = "marketplace")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class MarketPlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "markertplace_id", nullable = false, updatable = false)
    private Long markerPlaceID;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "email")
    private String email;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "owner_first_name")
    private String ownerFirstName;

    @Column(name = "owner_last_name")
    private String ownerLastName;

    @Column(name = "verification_doc")
    private String verificationDoc;

    @ElementCollection
    @CollectionTable(name = "social_media_links", joinColumns = @JoinColumn(name = "marketplace_id"))
    @MapKeyColumn(name = "social_media")
    @Column(name = "url")
    private Map<String, String> socialMediaLinks;

    @ElementCollection
    @CollectionTable(name = "opening_hours", joinColumns = @JoinColumn(name = "marketplace_id"))
    @MapKeyColumn(name = "day")
    private Map<String, TimeRange> openingHours;

    // Define the TimeRange
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeRange {
        @Column(name = "start_time")
        private String startTime;

        @Column(name = "end_time")
        private String endTime;
    }
}
