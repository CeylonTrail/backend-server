package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.marketplace.MarketPlaceDTO;
import com.ceylontrail.backend_server.service.MarkerPlaceService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/marketplace")
public class MarkerPlaceController {

    @Autowired
    private MarkerPlaceService markerPlaceService;


    @PostMapping(path = "/set-marketplace")
    public StandardResponse setMarket(@RequestBody  MarketPlaceDTO marketPlaceDTO){
        System.out.println(marketPlaceDTO);
        return markerPlaceService.setupMarket(marketPlaceDTO);
    }


}
