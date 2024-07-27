package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.service.GooglePlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Service
public class GooglePlacesServiceIMPL implements GooglePlacesService {

    @Value("${google.places.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getPlaces(String location) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&radius=1500&type=restaurant&key=" + apiKey;
        System.out.println(apiKey);
        System.out.println(location);
        return restTemplate.getForObject(url, String.class);

    }
}
