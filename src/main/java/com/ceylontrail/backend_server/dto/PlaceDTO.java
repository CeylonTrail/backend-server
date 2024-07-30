package com.ceylontrail.backend_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class PlaceDTO {

    private String placeId;
    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private String photoUrl;
    private double rating;

}
