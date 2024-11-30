package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.controller.AuthController;
import com.ceylontrail.backend_server.dto.EventDTO;
import com.ceylontrail.backend_server.dto.response.ResponseGetAllTripDTO;
import com.ceylontrail.backend_server.dto.requests.RequestTripSaveDTO;
import com.ceylontrail.backend_server.entity.*;
import com.ceylontrail.backend_server.repo.*;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.PlacesService;
import com.ceylontrail.backend_server.service.TripService;
import com.ceylontrail.backend_server.util.StandardResponse;
import com.ceylontrail.backend_server.util.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    private ImageRepo imageRepo;
    @Autowired
    private PlacesService placesService;
    @Autowired
    private SavedTripRepo savedTripRepo;



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

            if(imageRepo.existsByFilename(trip.getDestination().toLowerCase()+".jpg")){
                ImageEntity image = imageRepo.findByFilename(trip.getDestination().toLowerCase()+".jpg");
                System.out.println("image "+ image);
                String imgUrl = image.getUrl();
                System.out.println("Image in the db");
                trip.setImageURL(imgUrl);
            }else{
                placesService.searchPlaceByNameFromAPI(requestTripSaveDTO.getDestination());
                ImageEntity image = imageRepo.findByFilename(requestTripSaveDTO.getDestination().toLowerCase()+".jpg");
                String imgUrl = image.getUrl();
                trip.setImageURL(imgUrl);
            }


            tripRepo.save(trip);

            if(tripRepo.existsById(trip.getTripId())){
                List<EventEntity> eventList= mapper.DtoListToEntityList(requestTripSaveDTO.getEventSet());
                for(int i=0;i< eventList.size();i++){
                    eventList.get(i).setTrip(trip);

                    PlaceEntity placeEntity = mapper.placeDtoToEntity(eventList.get(i).getPlace());
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
                        tripEntity.getImageURL(),
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

    @Override
    public StandardResponse getTrip(int tripId) {
        List<EventDTO> eventDTOS = null;
        if (tripRepo.existsById(tripId)) {
            List<EventEntity> eventList = eventRepo.findAllByTrip_TripId(tripId);
            eventDTOS = mapper.eventListToDtoList(eventList);

        }
        return new StandardResponse(200, "retrieve success", eventDTOS);

    }

    @Override
    public StandardResponse deleteTrip(int tripId) {
        if(tripRepo.existsById(tripId)){
            tripRepo.deleteById(tripId);
            return new StandardResponse(200,"Success","Trip deleted successfully");
        }else{
            return new StandardResponse(404,"Success","Trip not found");
        }
    }

    @Override
    public StandardResponse saveCreatedTrip(int tripId) {
        int userId = authService.getAuthUserId();
        SavedTripEntity savedTripEntity = new SavedTripEntity();
        savedTripEntity.setTripId(tripId);
        savedTripEntity.setUserId(userId);

        savedTripRepo.save(savedTripEntity);
        return new StandardResponse(200,"Success",null);
    }

    @Override
    public StandardResponse getSavedTrips() {
        int userId = authService.getAuthUserId();
        List<SavedTripEntity> savedTripEntities = savedTripRepo.findAllByUserId(userId);
        List<Integer> tripIds = savedTripEntities.stream()
                .map(SavedTripEntity::getTripId)
                .toList();
        System.out.println("Trip IDs: " + tripIds);
        List<ResponseGetAllTripDTO> getAllTripDTOS = new ArrayList<>();
        for (Integer tripId : tripIds) {
            Optional<TripEntity> tripEntityOptional = tripRepo.findById(tripId); // Assuming `findById` is available
            if (tripEntityOptional.isPresent()) {
                TripEntity tripEntity = tripEntityOptional.get();
                ResponseGetAllTripDTO allTripDTO = new ResponseGetAllTripDTO(
                        tripEntity.getTripId(),
                        tripEntity.getDestination(),
                        tripEntity.getDayCount(),
                        tripEntity.getDescription(),
                        tripEntity.getImageURL(),
                        tripEntity.getCreatedAt(),
                        tripEntity.getUpdateAt()
                );
                getAllTripDTOS.add(allTripDTO);
            } else {
                System.err.println("Trip not found for ID: " + tripId);
            }
        }

        // Return the response with the DTO list
        return new StandardResponse(200, "Saved trips retrieved successfully", getAllTripDTOS);
    }



}
