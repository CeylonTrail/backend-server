package com.ceylontrail.backend_server.dto.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketPlaceDTO {
    private Long marketPlaceID;
    private String profileImage;
    private String coverImage;
    private String shopName;
    private String description;
    private String serviceType;
    private String email;
    private String contactNumber;
    private String address;
    private String ownerFirstName;
    private String ownerLastName;
    private String verificationDoc;
    private List<SocialMediaLinksDTO> socialMediaLinks;
    private List<OpeningHoursDTO> openingHours;
}
