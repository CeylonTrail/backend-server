package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.sp.SPSetupDTO;
import com.ceylontrail.backend_server.dto.sp.SubscriptionPurchaseDTO;
import com.ceylontrail.backend_server.entity.*;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
import com.ceylontrail.backend_server.repo.PaymentRepo;
import com.ceylontrail.backend_server.repo.ServiceProviderRepo;
import com.ceylontrail.backend_server.repo.SubscriptionPlanRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.ImageService;
import com.ceylontrail.backend_server.service.SPService;
import com.ceylontrail.backend_server.util.StandardResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SPServiceIMPL implements SPService {

    private final AuthService authService;
    private final ImageService imageService;

    private final UserRepo userRepo;
    private final ServiceProviderRepo spRepo;
    private final PaymentRepo paymentRepo;
    private final SubscriptionPlanRepo subscriptionPlanRepo;

    private List<OpeningHours> mapJsonToOpeningHours(String json) {
        try {
            List<OpeningHours> openingHoursList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String day = jsonObject.getString("day");
                String startTime = jsonObject.getString("startTime");
                String endTime = jsonObject.getString("endTime");
                OpeningHours openingHour = new OpeningHours(day, startTime, endTime);
                openingHoursList.add(openingHour);
            }
            return openingHoursList;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SocialMediaLinks> mapJsonToSocialMediaLinks(String json) {
        try {
            List<SocialMediaLinks> socialMediaLinksList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String link = jsonObject.getString("link");
                SocialMediaLinks socialMediaLink = new SocialMediaLinks(name, link);
                socialMediaLinksList.add(socialMediaLink);
            }
            return socialMediaLinksList;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public StandardResponse setup(SPSetupDTO spSetupDTO) {
        UserEntity user = userRepo.findByUserId(authService.getAuthUserId());
        ServiceProviderEntity sp = user.getServiceProvider();
        sp.setDescription(spSetupDTO.getDescription());
        sp.setContactNumber(spSetupDTO.getContactNumber());
        sp.setDescription(spSetupDTO.getDescription());
        sp.setOpeningHours(this.mapJsonToOpeningHours(spSetupDTO.getOpeningHours()));
        sp.setVerificationDocUrl(imageService.uploadImage(spSetupDTO.getVerificationDoc()).getUrl());
        sp.setVerificationStatus(VerificationStatusEnum.PENDING);
        sp.setVerificationStatusUpdatedAt(LocalDate.now());
        sp.setSubscriptionPlan(this.subscriptionPlanRepo.findBySubscriptionId(2L));
        sp.setSubscriptionDurationInDays(30);
        sp.setSubscriptionPurchaseDate(LocalDate.now());
        sp.setSubscriptionExpiryDate(LocalDate.now().plusDays(30));
        sp.setIsSetupComplete("YES");
        if (spSetupDTO.getSocialMediaLinks() != null)
            sp.setSocialMediaLinks(this.mapJsonToSocialMediaLinks(spSetupDTO.getSocialMediaLinks()));
        if (spSetupDTO.getProfilePicture() != null) {
            user.setProfilePictureUrl(imageService.uploadImage(spSetupDTO.getProfilePicture()).getUrl());
            userRepo.save(user);
        }
        if (spSetupDTO.getCoverPicture() != null)
            sp.setCoverPictureUrl(imageService.uploadImage(spSetupDTO.getCoverPicture()).getUrl());
        spRepo.save(sp);
        return new StandardResponse(200, "Service Provider setup success", null);
    }

    @Override
    @Transactional
    public StandardResponse purchaseSubscription(SubscriptionPurchaseDTO purchaseDTO) {
        SubscriptionPlanEntity subscriptionPlan = subscriptionPlanRepo.findBySubscriptionId(purchaseDTO.getSubscriptionId());
        if (subscriptionPlan == null)
            throw new EntityNotFoundException("Subscription Plan not found");
        ServiceProviderEntity sp = spRepo.findByUser(userRepo.findByUserId(authService.getAuthUserId()));
        sp.setSubscriptionPlan(subscriptionPlan);
        sp.setSubscriptionPurchaseDate(LocalDate.now());
        sp.setSubscriptionDurationInDays(purchaseDTO.getDurationInDays());
        sp.setSubscriptionExpiryDate(LocalDate.now().plusDays(purchaseDTO.getDurationInDays()));
        spRepo.save(sp);
        PaymentEntity payment = new PaymentEntity();
        payment.setServiceProvider(sp);
        payment.setType("SUBSCRIPTION");
        payment.setAmount(subscriptionPlan.getPrice());
        payment.setDate(sp.getSubscriptionPurchaseDate());
        paymentRepo.save(payment);
        return new StandardResponse(200, "Subscription purchase success", null);
    }

    @Override
    public void handleExpiredSubscriptions() {
        List<ServiceProviderEntity> expiredSubscriptions = spRepo.findBySubscriptionExpiryDateBefore(LocalDate.now());
        for (ServiceProviderEntity sp : expiredSubscriptions) {
            sp.setSubscriptionPlan(subscriptionPlanRepo.findBySubscriptionId(1L));
            sp.setSubscriptionPurchaseDate(null);
            sp.setSubscriptionDurationInDays(0);
            sp.setSubscriptionExpiryDate(null);
            spRepo.save(sp);
        }
    }
}
