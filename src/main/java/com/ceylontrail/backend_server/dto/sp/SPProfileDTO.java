package com.ceylontrail.backend_server.dto.sp;

import com.ceylontrail.backend_server.dto.advertisement.GetCardAdDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SPProfileDTO {

    private Long serviceProviderId;
    private String profilePictureUrl;
    private String coverPictureUrl;
    private String serviceName;
    private String serviceType;
    private String description;
    private String contactNumber;
    private String address;
    private String verificationStatus;
    private int publishedAddCount;
    private int maxAddCount;
    private List<GetCardAdDTO> ads;

}
