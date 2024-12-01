package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.requests.RequestTripSaveDTO;
import com.ceylontrail.backend_server.entity.TripEntity;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface TripService {
    StandardResponse saveTrip(RequestTripSaveDTO requestTripSaveDTO);

    StandardResponse allTrip();

    StandardResponse getTrip(int tripId);

    StandardResponse deleteTrip(int tripId);

    TripEntity initialTripCheck(int tripId);

    TripEntity initialTripAndUserCheck(int tripId);

}
