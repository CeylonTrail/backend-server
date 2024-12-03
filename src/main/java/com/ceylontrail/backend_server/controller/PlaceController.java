package com.ceylontrail.backend_server.controller;
import com.ceylontrail.backend_server.service.PlacesService;
import com.ceylontrail.backend_server.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            params = {"location"}
    )
    public StandardResponse getPlaces(
            @RequestParam(value = "location") String location

    ) {
       return googlePlacesService.getPlaces(location,10000,100);

    }

    @GetMapping(
            path = {"/get-all-places"}
    )
    public StandardResponse getPlaceByName(
    ){         return googlePlacesService.getAllPlaces();
    }



    @GetMapping(path = "/emergency")
    public StandardResponse emergencyPlaces(
            @RequestParam(value = "location") String location

    ){
        return googlePlacesService.getEmergencyPlaces(location);

    }

}
