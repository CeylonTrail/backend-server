package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.controller.AuthController;
import com.ceylontrail.backend_server.dto.response.ResponseGetAllTripDTO;
import com.ceylontrail.backend_server.dto.requests.RequestTripSaveDTO;
import com.ceylontrail.backend_server.entity.EventEntity;
import com.ceylontrail.backend_server.entity.PlaceEntity;
import com.ceylontrail.backend_server.entity.TripEntity;
import com.ceylontrail.backend_server.repo.EventRepo;
import com.ceylontrail.backend_server.repo.PlaceRepo;
import com.ceylontrail.backend_server.repo.TripRepo;
import com.ceylontrail.backend_server.service.AuthService;
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
    @Autowired
    private PlaceRepo placeRepo;
    @Autowired
    private AuthService authService;



    @Override
    public StandardResponse saveTrip(RequestTripSaveDTO requestTripSaveDTO) {
        int userId = authService.getAuthUserId();

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
                for(int i=0;i< eventList.size();i++){
                    eventList.get(i).setTrip(trip);

                    PlaceEntity placeEntity = mapper.DtoToEntity(eventList.get(i).getPlace());
                    if (placeEntity != null) {

                        boolean existsByPlace = placeRepo.existsByPlaceId(placeEntity.getPlaceId());
                            if(existsByPlace){
                                PlaceEntity existsEntity = placeRepo.findByPlaceId(placeEntity.getPlaceId());
                                eventList.get(i).setPlace(existsEntity);
                                System.out.println("location already has been saved");

                            }else{
                                placeRepo.save(placeEntity);
                                eventList.get(i).setPlace(placeEntity);

                            }
                    } else {
                        throw new IllegalArgumentException("Place details are missing for event: " + eventList.get(i).getDescription());
                    }
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


    @Override
    public StandardResponse allTrip() {
        int userId = authService.getAuthUserId();
        List<TripEntity> allTrips = tripRepo.findAllByUserId(userId);

        if(!allTrips.isEmpty()){
            List<ResponseGetAllTripDTO> getAllTripDTOS = new ArrayList<>();
            for (TripEntity tripEntity : allTrips) {
                ResponseGetAllTripDTO allTripDTO = new ResponseGetAllTripDTO(
                        tripEntity.getTripId(),
                        tripEntity.getDestination(),
                        tripEntity.getDayCount(),
                        tripEntity.getDescription(),
                        tripEntity.getCreatedAt(),
                        tripEntity.getUpdateAt()
                );
                getAllTripDTOS.add(allTripDTO);

            }
            return new StandardResponse(200, "success", getAllTripDTOS);

        }else{
            return new StandardResponse(200, "success", "No Planned Trips");
        }

    }
}
