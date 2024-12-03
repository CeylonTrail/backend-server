package com.ceylontrail.backend_server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SP {

    private int userId;
    private Long spId;
    private String serviceName;
    private String serviceType;
    private String username;
    private String firstname;
    private String lastname;
    private boolean accountStatus;
    private boolean setupState;
    private String verificationStatus;
    private String createdAt;

}
