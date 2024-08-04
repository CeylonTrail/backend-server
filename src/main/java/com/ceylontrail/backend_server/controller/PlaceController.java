package com.ceylontrail.backend_server.controller;
import com.ceylontrail.backend_server.service.PlacesService;
import com.ceylontrail.backend_server.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/v1/traveller")
public class PlaceController {
    @Autowired
    private PlacesService googlePlacesService;

    @Operation(
            description = "Get places",
            summary = "Nearby places for given radius",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Error",
                            responseCode = "400"
                    )
            })
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
//
//    @GetMapping(
//            path = "/get-place-by-name",
//            params = {"placename"}
//    )
//    public String getPlaceByName(@RequestParam(value = "placename") String placeName){
//         return googlePlacesService.searchPlaceByNameFromAPI(placeName);
//    }

}
