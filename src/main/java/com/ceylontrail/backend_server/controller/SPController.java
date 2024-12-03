package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.advertisement.AdvertisementDTO;
import com.ceylontrail.backend_server.dto.advertisement.EditAdDTO;
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
    @PostMapping("/advertisement")
    public StandardResponse createAdvertisement(@RequestBody AdvertisementDTO advertisementDTO){
        return this.spService.createAdvertisement(advertisementDTO);
    }

    @GetMapping("/advertisement")
    public StandardResponse getUserAdvertisements(){
        return this.spService.getUserAdvertisements();
    }

    @GetMapping("/advertisement/public")
    public StandardResponse getAllActiveAdvertisements(){
        return this.spService.getAllActiveAdvertisements();
    }

    @GetMapping("/advertisement/{advertisementId}")
    public StandardResponse getAdvertisement(@PathVariable Long advertisementId){
        return this.spService.getAdvertisement(advertisementId);
    }

    @PutMapping("/advertisement/{advertisementId}")
    public StandardResponse editAdvertisement(@PathVariable Long advertisementId, @RequestBody EditAdDTO editAdDTO){
        return this.spService.editAdvertisement(advertisementId, editAdDTO);
    }

    @DeleteMapping("/advertisement/{advertisementId}")
    public StandardResponse deleteAdvertisement( @PathVariable Long advertisementId){
        return this.spService.deleteAdvertisement(advertisementId);
    }

    @GetMapping("/advertisement/set-active/{advertisementId}")
    public StandardResponse setActive(@PathVariable Long advertisementId){
        return this.spService.setActive(advertisementId);
    }

    @GetMapping("/advertisement/set-inactive/{advertisementId}")
    public StandardResponse setInactive(@PathVariable Long advertisementId){
        return this.spService.setInactive(advertisementId);
    }

}
