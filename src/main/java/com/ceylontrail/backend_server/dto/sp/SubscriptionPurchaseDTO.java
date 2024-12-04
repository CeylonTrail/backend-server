package com.ceylontrail.backend_server.dto.sp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPurchaseDTO {

    private Long subscriptionId;
    private int durationInDays;

}
