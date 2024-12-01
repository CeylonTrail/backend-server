package com.ceylontrail.backend_server.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditSubscriptionDTO {

    private String name;
    private String description;
    private double price;
    private int adCount;

}
