package com.ceylontrail.backend_server.controller;
import com.ceylontrail.backend_server.dto.requests.RequestTripSaveDTO;
import com.ceylontrail.backend_server.service.TripService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/traveller")
public class TripController {
    @Autowired
    private TripService tripService;


    @PostMapping("/plan-trip")
    public StandardResponse saveTrip(@RequestBody RequestTripSaveDTO requestTripSaveDTO){

        return tripService.saveTrip(requestTripSaveDTO);
    }
    @GetMapping("/all-trip")
    public StandardResponse getAllTrip(){
        return tripService.allTrip();
    }

    @GetMapping("/get-trip/{trip_id}")
    public StandardResponse getTripById(@PathVariable(value = "trip_id") int tripId){
        return tripService.getTrip(tripId);

    }
    @DeleteMapping("/delete-trip/{trip_id}")
    public StandardResponse deleteTrip(@PathVariable(value = "trip_id") int tripId){
        return tripService.deleteTrip(tripId);
    }


    @GetMapping("/save-trip/{trip_id}")
    public StandardResponse saveTrip(@PathVariable(value = "trip_id")int tripId){
        return tripService.saveCreatedTrip(tripId);
    }

    @GetMapping("/saved-trips")
    public StandardResponse getSavedTrips(){
        return tripService.getSavedTrips();

    }


}
