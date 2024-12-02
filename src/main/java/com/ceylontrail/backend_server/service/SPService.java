package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.advertisement.AdvertisementDTO;
import com.ceylontrail.backend_server.dto.sp.SPSetupDTO;
import com.ceylontrail.backend_server.dto.sp.SubscriptionPurchaseDTO;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface SPService {
    StandardResponse setup(SPSetupDTO spSetupDTO);

    StandardResponse purchaseSubscription(SubscriptionPurchaseDTO purchaseDTO);

    void handleExpiredSubscriptions();

    StandardResponse createAdvertisement(AdvertisementDTO advertisementDTO);

    StandardResponse publishedAdvertisements();

    StandardResponse getAllAdvertisements();

    StandardResponse deleteAdvertisement(Long advertisementId);
}
