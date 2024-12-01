package com.ceylontrail.backend_server.dto.user;

import com.ceylontrail.backend_server.entity.OpeningHours;
import com.ceylontrail.backend_server.entity.SocialMediaLinks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoggedSPDTO {

    private String accessToken;
    private String role = "SERVICE_PROVIDER";
    private int userId;
    private Long serviceProviderId;
    private String username;
    private String email;
    private boolean setupState;
    private boolean accountState;
    private String verificationStatus;
    private String serviceName;
    private String serviceType;
    private String description;
    private String firstname;
    private String lastname;
    private String contactNumber;
    private String address;
    private String profilePictureUrl;
    private String coverPictureUrl;
    private List<SocialMediaLinks> socialMediaLinks;
    private List<OpeningHours> openingHours;

}