package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.advertisement.AdvertisementDTO;
import com.ceylontrail.backend_server.dto.sp.SPSetupDTO;
import com.ceylontrail.backend_server.dto.sp.SubscriptionPurchaseDTO;
import com.ceylontrail.backend_server.entity.*;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
import com.ceylontrail.backend_server.repo.*;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.ImageService;
import com.ceylontrail.backend_server.service.SPService;
import com.ceylontrail.backend_server.util.StandardResponse;
import com.ceylontrail.backend_server.util.mapper.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    private final AdvertisementRepo advertisementRepo;

    private final Mapper mapper;


    @Override
    @Transactional
    public StandardResponse setup(SPSetupDTO spSetupDTO) {
        UserEntity user = userRepo.findByUserId(authService.getAuthUserId());
        ServiceProviderEntity sp = user.getServiceProvider();
        sp.setDescription(spSetupDTO.getDescription());
        sp.setContactNumber(spSetupDTO.getContactNumber());
        sp.setDescription(spSetupDTO.getDescription());
        List<OpeningHours> openingHours = spSetupDTO.getOpeningHours().stream()
                .map(dto -> new OpeningHours(dto.getDay(), dto.getStartTime(), dto.getEndTime()))
                .toList();
        sp.setOpeningHours(openingHours);
        sp.setVerificationDocUrl(imageService.uploadImage(spSetupDTO.getVerificationDoc()).getUrl());
        sp.setVerificationStatus(VerificationStatusEnum.PENDING);
        sp.setSubscriptionPlan(this.subscriptionPlanRepo.findBySubscriptionId(2L));
        sp.setSubscriptionDurationInDays(30);
        sp.setSubscriptionPurchaseDate(LocalDate.now());
        sp.setSubscriptionExpiryDate(LocalDate.now().plusDays(30));
        sp.setIsSetupComplete("YES");
        if (spSetupDTO.getSocialMediaLinks() != null) {
            List<SocialMediaLinks> socialMediaLinks = spSetupDTO.getSocialMediaLinks().stream()
                    .map(dto -> new SocialMediaLinks(dto.getName(), dto.getLink()))
                    .collect(Collectors.toList());
            sp.setSocialMediaLinks(socialMediaLinks);
        }
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

    @Override
    public StandardResponse createAdvertisement(AdvertisementDTO advertisementDTO) {
        ServiceProviderEntity sp = spRepo.findByUser(userRepo.findByUserId(authService.getAuthUserId()));
        int publishedAdds = sp.getPublishedAddCount();
        int addCount = sp.getSubscriptionPlan().getAdCount();
        if(publishedAdds<addCount){
            AdvertisementEntity advertisement = new AdvertisementEntity(
                    advertisementDTO.getTitle(),
                    advertisementDTO.getDescription(),
                    advertisementDTO.getRateType(),
                    advertisementDTO.getRate(),
                    advertisementDTO.getDiscount()

            );
            advertisement.setServiceProvider(sp);
            advertisement.setUserId(authService.getAuthUserId());
            advertisementRepo.save(advertisement);
            publishedAdds++;
            sp.setPublishedAddCount(publishedAdds);
            spRepo.save(sp);

            return new StandardResponse(200,"success",null);
        }else{
            return new StandardResponse(500,"You have Reached post count",null);
        }
    }

    @Override
    public StandardResponse publishedAdvertisements() {
        int userId = authService.getAuthUserId();

        if(!advertisementRepo.existsByUserId(userId)){
            return new StandardResponse(404,"No published ads",null);
        }
        List<AdvertisementEntity> advertisementEntities = advertisementRepo.findAllByUserId(userId);
        List<AdvertisementDTO> advertisementDTOS = mapper.addEntityToDtoList(advertisementEntities);

        return new StandardResponse(200,"success",advertisementDTOS);
    }

    @Override
    public StandardResponse getAllAdvertisements() {
        List<AdvertisementEntity> advertisementEntities = advertisementRepo.findAll();
        List<AdvertisementDTO> advertisementDTOS = mapper.addEntityToDtoList(advertisementEntities);

        return new StandardResponse(200,"success",advertisementDTOS);
    }

    @Override
    public StandardResponse deleteAdvertisement(Long advertisementId) {
        ServiceProviderEntity sp = spRepo.findByUser(userRepo.findByUserId(authService.getAuthUserId()));

        advertisementRepo.deleteById(advertisementId);
        int publishedAdds = sp.getPublishedAddCount();
        publishedAdds--;
        sp.setPublishedAddCount(publishedAdds);
        spRepo.save(sp);

        return new StandardResponse(200,"success",null);
    }
}
