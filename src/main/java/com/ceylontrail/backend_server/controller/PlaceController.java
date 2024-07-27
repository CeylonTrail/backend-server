package com.ceylontrail.backend_server.controller;
import com.ceylontrail.backend_server.service.GooglePlacesService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/auth/traveller")
public class PlaceController {
    @Autowired
    private GooglePlacesService googlePlacesService;

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

}
