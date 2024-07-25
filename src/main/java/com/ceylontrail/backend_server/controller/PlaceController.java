package com.ceylontrail.backend_server.controller;
import com.ceylontrail.backend_server.service.GooglePlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/auth/traveller")
public class PlaceController {
    @Autowired
    private GooglePlacesService googlePlacesService;

    @GetMapping("/places")
    public String getPlaces(@RequestParam String location) {
        return googlePlacesService.getPlaces(location);
    }

}
