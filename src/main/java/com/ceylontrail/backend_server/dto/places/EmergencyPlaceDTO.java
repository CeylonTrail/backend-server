package com.ceylontrail.backend_server.dto.places;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class EmergencyPlaceDTO {
    private String placeId;
    private String name;
    private double latitude;
    private double longitude;
}
