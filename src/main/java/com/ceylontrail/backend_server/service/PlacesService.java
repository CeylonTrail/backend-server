package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.util.StandardResponse;

public interface PlacesService {

    String getCoordinates(String location);
    StandardResponse getPlaces(String location, int radius, int count);
}
