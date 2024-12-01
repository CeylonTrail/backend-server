package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.admin.GetSPDTO;
import com.ceylontrail.backend_server.dto.admin.GetTravellersDTO;
import com.ceylontrail.backend_server.dto.admin.SP;
import com.ceylontrail.backend_server.dto.admin.Traveller;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.entity.enums.ServiceProviderTypeEnum;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
import com.ceylontrail.backend_server.repo.PostRepo;
import com.ceylontrail.backend_server.repo.ServiceProviderRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.AdminService;
import com.ceylontrail.backend_server.service.ImageService;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.service.UserService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AdminServiceIMPL implements AdminService {

    private final UserService userService;
    private final PostService postService;
    private final ImageService imageService;

    private final UserRepo userRepo;
    private final PostRepo postRepo;
    private final ServiceProviderRepo spRepo;

    private Traveller mapToTraveller(UserEntity user) {
        Traveller traveller = new Traveller();
        traveller.setUserId(user.getUserId());
        traveller.setFirstname(user.getFirstname());
        traveller.setLastname(user.getLastname());
        traveller.setEmail(user.getEmail());
        traveller.setUsername(user.getUsername());
        traveller.setCreatedAt(user.getCreatedAt());
        traveller.setAccountStatus(user.getActivationToken() == null);
        return traveller;
    }

    private SP mapToSP(ServiceProviderEntity serviceProvider) {
        SP sp = new SP();
        sp.setUserId(serviceProvider.getUser().getUserId());
        sp.setSpId(serviceProvider.getServiceProviderId());
        sp.setServiceName(serviceProvider.getServiceName());
        if (Objects.equals(serviceProvider.getServiceType(), ServiceProviderTypeEnum.ACCOMMODATION))
            sp.setServiceType(String.valueOf(ServiceProviderTypeEnum.ACCOMMODATION));
        else if (Objects.equals(serviceProvider.getServiceType(), ServiceProviderTypeEnum.RESTAURANT))
            sp.setServiceType(String.valueOf(ServiceProviderTypeEnum.RESTAURANT));
        else if (Objects.equals(serviceProvider.getServiceType(), ServiceProviderTypeEnum.EQUIPMENT))
            sp.setServiceType(String.valueOf(ServiceProviderTypeEnum.EQUIPMENT));
        else
            sp.setServiceType(String.valueOf(ServiceProviderTypeEnum.OTHER));
        sp.setUsername(serviceProvider.getUser().getUsername());
        sp.setFirstname(serviceProvider.getUser().getFirstname());
        sp.setLastname(serviceProvider.getUser().getLastname());
        sp.setAccountStatus(serviceProvider.getUser().getActivationToken() == null);
        sp.setCreatedAt(serviceProvider.getUser().getCreatedAt());
        if (Objects.equals(serviceProvider.getIsSetupComplete(), "NO")) {
            sp.setSetupState(false);
            sp.setVerificationStatus("-");
        } else {
            sp.setSetupState(true);
            if (Objects.equals(serviceProvider.getVerificationStatus(), VerificationStatusEnum.PENDING))
                sp.setVerificationStatus(String.valueOf(VerificationStatusEnum.PENDING));
            else if (Objects.equals(serviceProvider.getVerificationStatus(), VerificationStatusEnum.REJECTED))
                sp.setVerificationStatus(String.valueOf(VerificationStatusEnum.REJECTED));
            else
                sp.setVerificationStatus(String.valueOf(VerificationStatusEnum.APPROVED));
        }
        return sp;
    }

    @Override
    public StandardResponse getTravellers(String key, int pageNumber, int pageSize) {
        Page<UserEntity> results = userRepo.searchTravellers(key, PageRequest.of(pageNumber - 1, pageSize));
        GetTravellersDTO dto = new GetTravellersDTO();
        dto.setPageNumber(results.getPageable().getPageNumber() + 1);
        dto.setPageSize(results.getPageable().getPageSize());
        dto.setTotalElements((int) results.getTotalElements());
        dto.setTotalPages(results.getTotalPages());
        List<Traveller> pageTravellers = new ArrayList<>();
        for (UserEntity user : results.getContent()) { pageTravellers.add(this.mapToTraveller(user)); }
        dto.setTravellers(pageTravellers);
        return new StandardResponse(200, "Travellers fetched successfully", dto);
    }

    @Override
    public StandardResponse getTraveller(int userId) {
        return null;
    }

    @Override
    public StandardResponse deleteTraveller(int userId) {
        UserEntity user = userService.initialUserCheck(userId);
        userRepo.deleteById(user.getUserId());
        return new StandardResponse(200, "Traveller deleted successfully", null);
    }

    @Override
    public StandardResponse getSPs(String key, int pageNumber, int pageSize) {
        Page<ServiceProviderEntity> results = spRepo.searchSPs(key, PageRequest.of(pageNumber - 1, pageSize));
        GetSPDTO dto = new GetSPDTO();
        dto.setPageNumber(results.getPageable().getPageNumber() + 1);
        dto.setPageSize(results.getPageable().getPageSize());
        dto.setTotalElements((int) results.getTotalElements());
        dto.setTotalPages(results.getTotalPages());
        List<SP> pageSPs = new ArrayList<>();
        for (ServiceProviderEntity serviceProvider : results.getContent()) { pageSPs.add(this.mapToSP(serviceProvider)); }
        dto.setSPs(pageSPs);
        return new StandardResponse(200, "SPs fetched successfully", dto);
    }

    @Override
    public StandardResponse getSP(int userId) {
        return null;
    }

    @Override
    public StandardResponse deleteSP(int userId) {
        UserEntity user = userService.initialUserCheck(userId);
        userRepo.deleteById(user.getUserId());
        return new StandardResponse(200, "Service Provider deleted successfully", null);
    }

    @Override
    public StandardResponse deletePost(Long postId) {
        PostEntity post = postService.initialPostCheck(postId);
        imageService.deletePostImages(post.getImages());
        postRepo.delete(post);
        return new StandardResponse(200, "Post deleted successfully", null);
    }
}
