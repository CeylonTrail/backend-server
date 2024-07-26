package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.controller.AuthController;
import com.ceylontrail.backend_server.dto.requests.RequestTripSaveDTO;
import com.ceylontrail.backend_server.entity.EventEntity;
import com.ceylontrail.backend_server.entity.TripEntity;
import com.ceylontrail.backend_server.repo.EventRepo;
import com.ceylontrail.backend_server.repo.TripRepo;
import com.ceylontrail.backend_server.service.TripService;
import com.ceylontrail.backend_server.util.StandardResponse;
import com.ceylontrail.backend_server.util.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TripServiceIMPL implements TripService {

    @Autowired
    private AuthController authController;

    @Autowired
    private TripRepo tripRepo;
    @Autowired
    private Mapper mapper;
    @Autowired
    private EventRepo eventRepo;


    @Override
    public StandardResponse saveTrip(RequestTripSaveDTO requestTripSaveDTO) {
        int userId = authController.getAuthenticatedUserId();
        if(!requestTripSaveDTO.getEventSet().isEmpty()) {
            TripEntity trip = new TripEntity(
                    requestTripSaveDTO.getDestination(),
                    requestTripSaveDTO.getDayCount(),
                    requestTripSaveDTO.getDescription(),
                    requestTripSaveDTO.getCreatedAt(),
                    requestTripSaveDTO.getUpdateAt()
            );
            trip.setUserId(userId);
            tripRepo.save(trip);

            if(tripRepo.existsById(trip.getTripId())){
                List<EventEntity> eventList= mapper.DtoListToEntityList(requestTripSaveDTO.getEventSet());
                for (EventEntity eventEntity : eventList) {
                    eventEntity.setTrip(trip);
                }

                if(!eventList.isEmpty()){
                    eventRepo.saveAll(eventList);
                }
            }
            return new StandardResponse(200,"Trip saved",requestTripSaveDTO);
        }else{
            return new StandardResponse(404,"Event data is empty",requestTripSaveDTO);
        }



    }
}
