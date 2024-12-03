package com.ceylontrail.backend_server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSP {

    private String serviceName;
    private String serviceType;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String createdAt;
    private String accountStatus;
    private String setupStatus;
    private String verificationStatus;
    private String verificationStatusUpdatedAt;
    private String description;
    private String contactNumber;
    private String address;
    private String subscriptionPlan;
    private String subscriptionPurchaseDate;
    private String subscriptionExpiryDate;

}
