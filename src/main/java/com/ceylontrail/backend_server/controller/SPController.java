package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.sp.SPSetupDTO;
import com.ceylontrail.backend_server.dto.sp.SubscriptionPurchaseDTO;
import com.ceylontrail.backend_server.service.SPService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/sp")
public class SPController {

    private final SPService spService;

    @PostMapping(path = "/setup")
    public StandardResponse setup(@ModelAttribute SPSetupDTO spSetupDTO){
        return this.spService.setup(spSetupDTO);
    }

    @PostMapping("/purchase/subscription")
    public StandardResponse purchaseSubscription(@RequestBody SubscriptionPurchaseDTO purchaseDTO) {
        return this.spService.purchaseSubscription(purchaseDTO);
    }

}
