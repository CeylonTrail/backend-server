package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.entity.PlaceEntity;
import com.ceylontrail.backend_server.service.GooglePlacesService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GooglePlacesServiceIMPL implements GooglePlacesService {

    @Value("${google.places.api.key}")
    private String apiKey;

    @Value("${google.places.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getCoordinates(String location) {
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s", location, apiKey);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            if (!results.isEmpty()) {
                Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
                Map<String, Object> locationMap = (Map<String, Object>) geometry.get("location");
                String latitude = locationMap.get("lat").toString();
                String longitude = locationMap.get("lng").toString();
                return latitude + "," + longitude;
            }
        }
        return null;
    }


    @Override
    public StandardResponse getPlaces(String location, int radius, int count) {
        String coordinates = getCoordinates(location);
        if (coordinates == null) {
            throw new IllegalArgumentException("Unable to get coordinates for location: " + location);
        }
        String url = String.format("%s?location=%s&radius=%d&type=tourist_attraction&key=%s", apiUrl, coordinates, radius, apiKey);
        int resultsCount = 0;
        List<PlaceEntity> allPlaces = new ArrayList<>();

        PlaceEntity placeEntity = null;
        while (url != null && resultsCount < count) {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> places = (List<Map<String, Object>>) response.get("results");
                for (Map<String, Object> placeData : places) {
                    if (resultsCount >= count) break;
                    Map<String, Object> geometry = (Map<String, Object>) placeData.get("geometry");
                    Map<String, Object> locationData = (Map<String, Object>) geometry.get("location");
                    double rating = placeData.containsKey("rating") ? ((Number) placeData.get("rating")).doubleValue() : 0.0;

                    if(rating>0){
                        placeEntity = new PlaceEntity(
                                (String) placeData.get("place_id"),
                                (String) placeData.get("name"),
                                (double) locationData.get("lat"),
                                (double) locationData.get("lng"),
                                rating
                        );
                        allPlaces.add(placeEntity);

                        System.out.println(placeData);
                        System.out.println(placeEntity);

                        resultsCount++;

                    }

                }

                if (response.containsKey("next_page_token")) {
                    String nextPageToken = (String) response.get("next_page_token");
                    url = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=%s&key=%s", nextPageToken, apiKey);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    url = null;
                }
            } else {
                url = null;
            }

        }
        return new StandardResponse(200, "sucess", allPlaces);

    }
}
