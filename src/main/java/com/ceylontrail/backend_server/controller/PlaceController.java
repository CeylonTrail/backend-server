package com.ceylontrail.backend_server.controller;
import com.ceylontrail.backend_server.service.PlacesService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/v1/traveller")
public class PlaceController {
    @Autowired
    private PlacesService googlePlacesService;

    @GetMapping(
            path = {"/places"},
            params = {"location","radius","count"}
    )
    public StandardResponse getPlaces(
            @RequestParam(value = "location") String location,
            @RequestParam(value = "radius") int radius,
            @RequestParam(value = "count") int count

    ) {
       return googlePlacesService.getPlaces(location,radius,count);

    }

    @GetMapping(
            path = "/get-place-by-name",
            params = {"placename"}
    )
    public String getPlaceByName(@RequestParam(value = "placename") String placeName){
        return googlePlacesService.searchPlaceByNameFromAPI(placeName).toString();
    }

}
