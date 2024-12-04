package com.ceylontrail.backend_server.util.mapper;

import com.ceylontrail.backend_server.dto.EventDTO;
import com.ceylontrail.backend_server.dto.places.PlaceDTO;
import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.entity.EventEntity;
import com.ceylontrail.backend_server.entity.PlaceEntity;
import com.ceylontrail.backend_server.entity.UserEntity;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    //Auth Controller
    UserEntity registerDtoToEntity(RegisterDTO registerDTO);

    //Trip Controller
    List<EventEntity> DtoListToEntityList(List<EventDTO> eventSet);


    //Places Controller
    PlaceEntity placeDtoToEntity(PlaceEntity place);
    List<PlaceDTO> placesEntityListToDtoList(List<PlaceEntity> places);
//    List<PlaceDTO> pageToList(Page<PlaceEntity> placePage);

    //Event Controller
    List<EventDTO> eventListToDtoList(List<EventEntity> eventList);





}
