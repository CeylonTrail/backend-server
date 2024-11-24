package com.ceylontrail.backend_server.dto.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommunityTripDTO {

    private int tripId;
    private String destination;
    private int dayCount;

}
