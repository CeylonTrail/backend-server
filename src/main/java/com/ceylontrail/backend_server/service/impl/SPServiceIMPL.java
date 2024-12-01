package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.sp.SPSetupDTO;
import com.ceylontrail.backend_server.entity.OpeningHours;
import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import com.ceylontrail.backend_server.entity.SocialMediaLinks;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
import com.ceylontrail.backend_server.repo.ServiceProviderRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.ImageService;
import com.ceylontrail.backend_server.service.SPService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SPServiceIMPL implements SPService {

    private final AuthService authService;
    private final ImageService imageService;

    private final UserRepo userRepo;
    private final ServiceProviderRepo spRepo;

    @Override
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
}
