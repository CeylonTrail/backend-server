package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.entity.PlaceEntity;
import com.ceylontrail.backend_server.repo.PlaceRepo;
import com.ceylontrail.backend_server.service.PlacesService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PlacesServiceIMPL implements PlacesService {

    @Value("${google.places.api.key}")
    private String apiKey;

    @Value("${google.places.api.url}")
    private String apiUrl;

    @Value("${open.api.key}")
    private String openAiApiKey;

    @Value("${open.api.url}")
    private String openAiApiUrl;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PlaceRepo placeRepo;

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
                    List<String> types = (List<String>) placeData.get("types");
                    if (
                            types == null || !types.contains("tourist_attraction") || types.contains("lodging")
                                    || types.contains("travel_agency") || types.contains("store")
                    ) {
                        continue;
                    }
                    if (resultsCount >= count) break;
                    Map<String, Object> geometry = (Map<String, Object>) placeData.get("geometry");
                    Map<String, Object> locationData = (Map<String, Object>) geometry.get("location");
                    double rating = placeData.containsKey("rating") ? ((Number) placeData.get("rating")).doubleValue() : 0.0;
                    int user_rating = placeData.containsKey("user_ratings_total")?((Number) placeData.get("user_ratings_total")).intValue():0;

                    if(rating>2 && user_rating>50) {
                        String photoUrl = null;
                        if (placeData.containsKey("photos")) {
                            List<Map<String, Object>> photos = (List<Map<String, Object>>) placeData.get("photos");
                            if (!photos.isEmpty()) {
                                String photoReference = (String) photos.get(0).get("photo_reference");
                                photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s", photoReference, apiKey);
                            }
                        }
                        placeEntity = new PlaceEntity(
                                (String) placeData.get("place_id"),
                                (String) placeData.get("name"),
                                (double) locationData.get("lat"),
                                (double) locationData.get("lng"),
                                generateDescription((String) placeData.get("name"), (String) placeData.get("vicinity")),
                                photoUrl,
                                rating

                        );

                        boolean existByPlaceID =placeRepo.existsByPlaceId(placeEntity.getPlaceId());
                        if(existByPlaceID){
                            System.out.println("place already in the db");
                        }else{
                            placeRepo.save(placeEntity);

                        }

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
        return new StandardResponse(200, "success", allPlaces);

    }

    private String generateDescription(String name, String address) {
        String prompt = String.format(
                "Give a 50 word description for following tourist attraction place in Sri Lanka for a travel app. Return only the description.The place name is: %s. located in: %s",
                name,address);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", Arrays.asList(
                new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", "You are a helpful assistant that generate descriptions for places.");
                }},
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        ));
        requestBody.put("max_tokens", 100);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(openAiApiUrl, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String description = (String) message.get("content");
                    return description;
                }
            }
        }
        return ("No description found");
    }

    public String searchPlaceByNameFromAPI(String placeName) {
        String url = String.format(
                "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=%s&inputtype=textquery&fields=place_id,name,formatted_address&key=%s",
                placeName, apiKey
        );
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        System.out.println("response: " + response);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("candidates")) {
                Map<String, Object> candidate = ((List<Map<String, Object>>) responseBody.get("candidates")).get(0);
                String placeId = (String) candidate.get("place_id");
                String photoUrl = getPlacePhotoUrlFromAPI(placeId);
                return photoUrl;
            }
        }
        return null;
    }

    public String getPlacePhotoUrlFromAPI(String placeId) {
        String url = String.format("https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&key=%s", placeId, apiKey);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("result")) {
                Map<String, Object> place = (Map<String, Object>) responseBody.get("result");

                if (place.containsKey("photos")) {
                    List<Map<String, Object>> photos = (List<Map<String, Object>>) place.get("photos");

                    System.out.println(photos);
                    if (!photos.isEmpty()) {
                        String photoReference = (String) photos.get(0).get("photo_reference");
                        String photoUrl = String.format(
                                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                                photoReference, apiKey
                        );
                        return photoUrl; // Return the photo URL
                    }
                }
            }
        }
        return null;
    }



}
