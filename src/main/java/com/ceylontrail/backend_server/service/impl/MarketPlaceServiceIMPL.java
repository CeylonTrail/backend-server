package com.ceylontrail.backend_server.service.impl;
import com.ceylontrail.backend_server.dto.marketplace.MarketPlaceDTO;
import com.ceylontrail.backend_server.entity.MarketPlaceEntity;
import com.ceylontrail.backend_server.entity.OpeningHours;
import com.ceylontrail.backend_server.entity.SocialMediaLinks;
import com.ceylontrail.backend_server.repo.MarketPlaceRepo;
import com.ceylontrail.backend_server.service.ImageService;
import com.ceylontrail.backend_server.service.MarkerPlaceService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketPlaceServiceIMPL implements MarkerPlaceService {
    @Autowired
    private MarketPlaceRepo marketPlaceRepo;

    @Autowired
    private ImageService imageService;

    @Override
    public StandardResponse setupMarket(MarketPlaceDTO marketPlaceDTO) {
        System.out.println(marketPlaceDTO);
        List<SocialMediaLinks> socialMediaLinks = marketPlaceDTO.getSocialMediaLinks().stream()
                .map(dto -> new SocialMediaLinks(dto.getName(), dto.getLink()))
                .collect(Collectors.toList());

        List<OpeningHours> openingHours = marketPlaceDTO.getOpeningHours().stream()
                .map(dto -> new OpeningHours(dto.getDay(), dto.getStartTime(), dto.getEndTime()))
                .toList();


        MarketPlaceEntity marketPlaceEntity = new MarketPlaceEntity(
                marketPlaceDTO.getProfileImage(),
                marketPlaceDTO.getCoverImage(),
                marketPlaceDTO.getShopName(),
                marketPlaceDTO.getDescription(),
                marketPlaceDTO.getServiceType(),
                marketPlaceDTO.getEmail(),
                marketPlaceDTO.getContactNumber(),
                marketPlaceDTO.getAddress(),
                marketPlaceDTO.getOwnerFirstName(),
                marketPlaceDTO.getOwnerLastName(),
                marketPlaceDTO.getVerificationDoc(),
                socialMediaLinks,
                openingHours
        );

        if(marketPlaceDTO.getProfileImage()!=null){
            //imageService.uploadImage(marketPlaceDTO.getProfileImage());

        }
        marketPlaceRepo.save(marketPlaceEntity);


        return new StandardResponse(200,"succsess",marketPlaceEntity);
    }
}
