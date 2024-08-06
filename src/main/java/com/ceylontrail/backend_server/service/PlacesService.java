package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.util.StandardResponse;

import java.util.Map;

public interface PlacesService {

    String getCoordinates(String location);
    StandardResponse getPlaces(String location, int radius, int count);

    void searchPlaceByNameFromAPI(String placeName);

    StandardResponse getAllPlaces(int page, int size);
}
