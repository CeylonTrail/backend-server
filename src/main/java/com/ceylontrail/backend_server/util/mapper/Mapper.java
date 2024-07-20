package com.ceylontrail.backend_server.util.mapper;

import com.ceylontrail.backend_server.dto.RegisterDTO;
import com.ceylontrail.backend_server.entity.UserEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    UserEntity registerDtoToEntity(RegisterDTO registerDTO);
}
