package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.sp.SPSetupDTO;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface SPService {
    StandardResponse setup(SPSetupDTO spSetupDTO);
}
