package com.ceylontrail.backend_server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscribers {

    private String serviceName;
    private String serviceType;
    private String profilePictureUrl;

}
