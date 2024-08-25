package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.marketplace.MarketPlaceDTO;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface MarkerPlaceService {
    StandardResponse setupMarket(MarketPlaceDTO marketPlaceDTO);
}
