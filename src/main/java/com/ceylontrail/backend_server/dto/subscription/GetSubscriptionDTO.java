package com.ceylontrail.backend_server.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSubscriptionDTO {

    private Long subscriptionId;
    private String name;
    private String description;
    private double price;
    private int adCount;
    private List<String> features;

}
