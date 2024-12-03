package com.ceylontrail.backend_server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPendingSP {

    private Long serviceProviderId;
    private String profilePictureUrl;
    private String coverPictureUrl;
    private String serviceName;
    private String serviceType;
    private String description;
    private String contactNumber;
    private String address;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String createdAt;
    private String accountStatus;
    private String verificationStatus;
    private String verificationStatusUpdatedAt;
    private String verificationDocUrl;
    private String subscriptionPlan;
    private String subscriptionPurchaseDate;
    private String subscriptionExpiryDate;

}
