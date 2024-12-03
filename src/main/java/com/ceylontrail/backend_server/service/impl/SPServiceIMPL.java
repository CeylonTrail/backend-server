package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.advertisement.AdvertisementDTO;
import com.ceylontrail.backend_server.dto.advertisement.EditAdDTO;
import com.ceylontrail.backend_server.dto.advertisement.GetAdDTO;
import com.ceylontrail.backend_server.dto.sp.SPSetupDTO;
import com.ceylontrail.backend_server.dto.sp.SubscriptionPurchaseDTO;
import com.ceylontrail.backend_server.entity.*;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.exception.UnauthorizedException;
import com.ceylontrail.backend_server.repo.*;
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
    private final PaymentRepo paymentRepo;
    private final AdvertisementRepo adRepo;
    private final ServiceProviderRepo spRepo;
    private final SubscriptionPlanRepo subscriptionPlanRepo;

    private AdvertisementEntity initialAdCheck(Long advertisementId) {
        AdvertisementEntity ad = adRepo.findByAdvertisementId(advertisementId);
        if (ad == null) {
            throw new NotFoundException("Advertisement does not exist");
        }
        return ad;
    }

    private AdvertisementEntity initialAdAndUserCheck(Long advertisementId) {
        AdvertisementEntity ad = this.initialAdCheck(advertisementId);
        UserEntity loggedUser = userRepo.findByUserId(authService.getAuthUserId());
        if (loggedUser.getUserId() != ad.getServiceProvider().getUser().getUserId()) {
            throw new UnauthorizedException("Advertisement author is not logged in");
        }
        return ad;
    }

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

    private GetAdDTO mapToGetAdDTO(AdvertisementEntity ad) {
        GetAdDTO dto = new GetAdDTO();
        dto.setAdvertisementId(ad.getAdvertisementId());
        dto.setTitle(ad.getTitle());
        dto.setDescription(ad.getDescription());
        dto.setRateType(ad.getRateType());
        dto.setRate(ad.getRate());
        dto.setIsActive(ad.getIsActive());
        dto.setCreatedAt(ad.getCreatedAt().toLocalDate().toString());
        List<String> images = new ArrayList<>();
        for (ImageEntity image : ad.getImages()) {
            images.add(image.getUrl());
        }
        dto.setImages(images);
        return dto;
    }

    @Override
    @Transactional
    public StandardResponse setup(SPSetupDTO spSetupDTO) {
        UserEntity user = userRepo.findByUserId(authService.getAuthUserId());
        ServiceProviderEntity sp = user.getServiceProvider();
        sp.setDescription(spSetupDTO.getDescription());
        sp.setContactNumber(spSetupDTO.getContactNumber());
        sp.setAddress(spSetupDTO.getAddress());
        sp.setOpeningHours(this.mapJsonToOpeningHours(spSetupDTO.getOpeningHours()));
        sp.setVerificationDocUrl(imageService.uploadImage(spSetupDTO.getVerificationDoc()).getUrl());
        sp.setVerificationStatus(VerificationStatusEnum.PENDING);
        sp.setVerificationStatusUpdatedAt(LocalDate.now());
        sp.setPublishedAddCount(0);
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
    @Transactional
    public void handleExpiredSubscriptions() {
        List<ServiceProviderEntity> expiredSubscriptions = spRepo.findBySubscriptionExpiryDateBefore(LocalDate.now());
        for (ServiceProviderEntity sp : expiredSubscriptions) {
            List<AdvertisementEntity> adList = adRepo.findByServiceProvider(sp);
            for (AdvertisementEntity ad : adList) {
                ad.setIsActive("NO");
                adRepo.save(ad);
            }
            sp.setSubscriptionPlan(subscriptionPlanRepo.findBySubscriptionId(1L));
            sp.setSubscriptionPurchaseDate(null);
            sp.setSubscriptionDurationInDays(0);
            sp.setSubscriptionExpiryDate(null);
            sp.setPublishedAddCount(0);
            spRepo.save(sp);
        }
    }

    @Override
    @Transactional
    public StandardResponse createAdvertisement(AdvertisementDTO adDTO) {
        ServiceProviderEntity sp = spRepo.findByUser(userRepo.findByUserId(authService.getAuthUserId()));
        if (sp.getVerificationStatus() != VerificationStatusEnum.APPROVED)
            return new StandardResponse(400,"Account need to be verified",null);
        if (sp.getPublishedAddCount() >= sp.getSubscriptionPlan().getAdCount())
            return new StandardResponse(400,"Maximum advertisement count reached",null);
        AdvertisementEntity ad = new AdvertisementEntity();
        ad.setServiceProvider(sp);
        ad.setTitle(adDTO.getTitle());
        ad.setDescription(adDTO.getDescription());
        ad.setRateType(adDTO.getRateType());
        ad.setRate(adDTO.getRate());
        ad.setIsActive("YES");
        adRepo.save(ad);
        if (adDTO.getImages() != null) {
            ad.setImages(imageService.UploadAdImages(ad, adDTO.getImages()));
            adRepo.save(ad);
        }
        sp.setPublishedAddCount(sp.getPublishedAddCount() + 1);
        spRepo.save(sp);
        return new StandardResponse(200, "Advertisement created successfully", null);
    }

    @Override
    public StandardResponse getAdvertisement(Long advertisementId) {
        AdvertisementEntity ad = this.initialAdCheck(advertisementId);
        GetAdDTO dto = this.mapToGetAdDTO(ad);
        return new StandardResponse(200,"Advertisement fetched successfully", dto);
    }

    @Override
    public StandardResponse getUserAdvertisements() {
        List<AdvertisementEntity> adList = adRepo.findByServiceProvider(spRepo.findByUser(userRepo.findByUserId(authService.getAuthUserId())));
        List<GetAdDTO> dto  = new ArrayList<>();
        for (AdvertisementEntity ad : adList) {
            dto.add(this.mapToGetAdDTO(ad));
        }
        return new StandardResponse(200,"User advertisements fetched successfully", dto);
    }

    @Override
    public StandardResponse getAllActiveAdvertisements() {
        List<GetAdDTO> dto  = new ArrayList<>();
        for (AdvertisementEntity ad : adRepo.findAllActiveAdvertisements()) {
            dto.add(this.mapToGetAdDTO(ad));
        }
        return new StandardResponse(200,"All advertisements fetched successfully", dto);
    }

    @Override
    public StandardResponse editAdvertisement(Long advertisementId, EditAdDTO adDTO) {
        AdvertisementEntity ad = this.initialAdAndUserCheck(advertisementId);
        ad.setTitle(adDTO.getTitle());
        ad.setDescription(adDTO.getDescription());
        ad.setRateType(adDTO.getRateType());
        ad.setRate(adDTO.getRate());
        adRepo.save(ad);
        return new StandardResponse(200, "Advertisement updated successfully", null);
    }

    @Override
    @Transactional
    public StandardResponse deleteAdvertisement(Long advertisementId) {
        AdvertisementEntity ad = this.initialAdAndUserCheck(advertisementId);
        imageService.deleteAdImages(ad.getImages());
        ServiceProviderEntity sp = ad.getServiceProvider();
        sp.setPublishedAddCount(sp.getPublishedAddCount() - 1);
        spRepo.save(sp);
        adRepo.delete(ad);
        return new StandardResponse(200,"Advertisement deleted successfully",null);
    }

    @Override
    @Transactional
    public StandardResponse setActive(Long advertisementId) {
        AdvertisementEntity ad = this.initialAdAndUserCheck(advertisementId);
        ServiceProviderEntity sp = ad.getServiceProvider();
        if (sp.getPublishedAddCount() >= sp.getSubscriptionPlan().getAdCount())
            return new StandardResponse(400,"Maximum advertisement count reached",null);
        if (ad.getIsActive().equals("YES"))
            return new StandardResponse(400,"Advertisement is already active",null);
        ad.setIsActive("YES");
        adRepo.save(ad);
        sp.setPublishedAddCount(sp.getPublishedAddCount() + 1);
        spRepo.save(sp);
        return new StandardResponse(200, "Advertisement is active now", null);
    }

    @Override
    @Transactional
    public StandardResponse setInactive(Long advertisementId) {
        AdvertisementEntity ad = this.initialAdAndUserCheck(advertisementId);
        ServiceProviderEntity sp = ad.getServiceProvider();
        if (ad.getIsActive().equals("NO"))
            return new StandardResponse(400,"Advertisement is already inactive",null);
        ad.setIsActive("NO");
        adRepo.save(ad);
        sp.setPublishedAddCount(sp.getPublishedAddCount() - 1);
        spRepo.save(sp);
        return new StandardResponse(200, "Advertisement is inactive now", null);
    }

}
