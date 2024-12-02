package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.advertisement.AdvertisementDTO;
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
    public StandardResponse setup(@RequestBody SPSetupDTO spSetupDTO){
        return this.spService.setup(spSetupDTO);
    }

    @PostMapping("/purchase/subscription")
    public StandardResponse purchaseSubscription(@RequestBody SubscriptionPurchaseDTO purchaseDTO) {
        return this.spService.purchaseSubscription(purchaseDTO);
    }
    @PostMapping("/create/advertisemnet")
    public StandardResponse createAdvertisement(@RequestBody AdvertisementDTO advertisementDTO){
        return this.spService.createAdvertisement(advertisementDTO);
    }

    @GetMapping("/view/advertisement")
    public StandardResponse publishedAdvertisements(){
        return this.spService.publishedAdvertisements();
    }

    @GetMapping("/all-advertisements")
    public StandardResponse allAds(){
        return this.spService.getAllAdvertisements();
    }

    @DeleteMapping("/delete-advertisement/{advertisement_id}")
    public StandardResponse deleteAd( @PathVariable(value = "advertisement_id") Long advertisementId){
        return this.spService.deleteAdvertisement(advertisementId);
    }

}
