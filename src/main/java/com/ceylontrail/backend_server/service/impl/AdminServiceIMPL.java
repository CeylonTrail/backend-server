package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.admin.*;
import com.ceylontrail.backend_server.dto.subscription.AddSubscriptionDTO;
import com.ceylontrail.backend_server.dto.subscription.EditSubscriptionDTO;
import com.ceylontrail.backend_server.dto.subscription.GetSubscriptionDTO;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.entity.ServiceProviderEntity;
import com.ceylontrail.backend_server.entity.SubscriptionPlanEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.entity.enums.ServiceProviderTypeEnum;
import com.ceylontrail.backend_server.entity.enums.VerificationStatusEnum;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.repo.*;
import com.ceylontrail.backend_server.service.AdminService;
import com.ceylontrail.backend_server.service.ImageService;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.service.UserService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceIMPL implements AdminService {

    private final UserService userService;
    private final PostService postService;
    private final ImageService imageService;

    private final UserRepo userRepo;
    private final PostRepo postRepo;
    private final ReportRepo reportRepo;
    private final PaymentRepo paymentRepo;
    private final ServiceProviderRepo spRepo;
    private final SubscriptionPlanRepo subscriptionRepo;

    private SubscriptionPlanEntity initialSubscriptionPlanCheck(Long subscriptionId) {
        SubscriptionPlanEntity subscription = subscriptionRepo.findBySubscriptionId(subscriptionId);
        if (subscription == null) {
            throw new NotFoundException("Subscription plan not found");
        }
        return subscription;
    }

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

    private ChartData getSubscriptionCountsForLastYear() {
        YearMonth startMonth = YearMonth.now().minusMonths(11);
        YearMonth endMonth = YearMonth.now();
        List<YearMonth> months = new ArrayList<>();
        for (YearMonth month = startMonth; !month.isAfter(endMonth); month = month.plusMonths(1)) {
            months.add(month);
        }
        List<Object[]> results = paymentRepo.countSubscriptionsByMonth(startMonth.atDay(1));
        Map<YearMonth, Integer> resultMap = results.stream()
                .collect(Collectors.toMap(
                        row -> YearMonth.of(((Number) row[0]).intValue(), ((Number) row[1]).intValue()),
                        row -> ((Number) row[2]).intValue()
                ));
        List<String> time = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        for (YearMonth month : months) {
            time.add(month.getYear() + "-" + month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            count.add(resultMap.getOrDefault(month, 0));
        }
        return new ChartData(time, count);
    }


    private List<SP> getLatestPendingBusinessProfiles(){
        List<ServiceProviderEntity> results = spRepo.findLatestPending(VerificationStatusEnum.PENDING, PageRequest.of(0, 3));
        return results.stream()
                .map(this::mapToSP)
                .toList();
    }

    private List<Subscriber> getLatestSubscribers() {
        List<ServiceProviderEntity> results = paymentRepo.findLatestSubscribers(PageRequest.of(0, 6));
        System.out.println(results);
        return results.stream()
                .map(sp -> {
                    Subscriber subscriber = new Subscriber();
                    subscriber.setServiceName(sp.getServiceName());
                    subscriber.setProfilePictureUrl(sp.getUser().getProfilePictureUrl());
                    if (Objects.equals(sp.getServiceType(), ServiceProviderTypeEnum.ACCOMMODATION))
                        subscriber.setServiceType(String.valueOf(ServiceProviderTypeEnum.ACCOMMODATION));
                    else if (Objects.equals(sp.getServiceType(), ServiceProviderTypeEnum.RESTAURANT))
                        subscriber.setServiceType(String.valueOf(ServiceProviderTypeEnum.RESTAURANT));
                    else if (Objects.equals(sp.getServiceType(), ServiceProviderTypeEnum.EQUIPMENT))
                        subscriber.setServiceType(String.valueOf(ServiceProviderTypeEnum.EQUIPMENT));
                    else
                        subscriber.setServiceType(String.valueOf(ServiceProviderTypeEnum.OTHER));
                    return subscriber;
                })
                .toList();
    }

    @Override
    public StandardResponse getDashboard() {
        DashboardDTO dto = new DashboardDTO();
        LocalDate startDate = LocalDate.now().minusDays(30);
        dto.setTotalUsers(Optional.of(userRepo.countUsers()).orElse(0));
        dto.setTotalTravellers(Optional.of(userRepo.countTravellers()).orElse(0));
        dto.setRecentUsers(Optional.of(userRepo.countRecentUsers(startDate.atStartOfDay())).orElse(0));
        dto.setTotalBusinessProfiles(Optional.of(spRepo.countBusinessProfiles(VerificationStatusEnum.APPROVED)).orElse(0));
        dto.setRecentBusinessProfiles(Optional.of(spRepo.countRecentBusinessProfiles(VerificationStatusEnum.APPROVED, startDate)).orElse(0));
        dto.setTotalReports(Optional.of(reportRepo.countReports()).orElse(0));
        dto.setRecentReports(Optional.of(reportRepo.countRecentReports(startDate.atStartOfDay())).orElse(0));
        dto.setTotalRevenue(Optional.ofNullable(paymentRepo.calculateTotalRevenue()).orElse(0.0));
        dto.setRecentRevenue(Optional.ofNullable(paymentRepo.calculateRevenueForRecentDays(startDate)).orElse(0.0));
        dto.setSubscriptionChartData(this.getSubscriptionCountsForLastYear());
        System.out.println(dto.getSubscriptionChartData());
        dto.setPendingBusinessProfiles(this.getLatestPendingBusinessProfiles());
        dto.setRecentSubscribers(this.getLatestSubscribers());
        return new StandardResponse(200, "Dashboard fetched successfully", dto);
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

    @Override
    public StandardResponse addSubscription(AddSubscriptionDTO subscriptionDTO) {
        SubscriptionPlanEntity subscription = new SubscriptionPlanEntity();
        subscription.setName(subscriptionDTO.getName());
        subscription.setDescription(subscriptionDTO.getDescription());
        subscription.setPrice(subscriptionDTO.getPrice());
        subscription.setAdCount(subscriptionDTO.getAdCount());
        subscriptionRepo.save(subscription);
        return new StandardResponse(200, "Subscription plan added successfully", null);
    }

    @Override
    public StandardResponse getSubscription(Long subscriptionId) {
        SubscriptionPlanEntity subscription = this.initialSubscriptionPlanCheck(subscriptionId);
        GetSubscriptionDTO dto = new GetSubscriptionDTO();
        dto.setSubscriptionId(subscriptionId);
        dto.setName(subscription.getName());
        dto.setDescription(subscription.getDescription());
        dto.setPrice(subscription.getPrice());
        dto.setAdCount(subscription.getAdCount());
        return new StandardResponse(200, "Subscription plan fetched successfully", dto);
    }

    @Override
    public StandardResponse editSubscription(Long subscriptionId, EditSubscriptionDTO subscriptionDTO) {
        SubscriptionPlanEntity subscription = this.initialSubscriptionPlanCheck(subscriptionId);
        subscription.setName(subscriptionDTO.getName());
        subscription.setDescription(subscriptionDTO.getDescription());
        subscription.setPrice(subscriptionDTO.getPrice());
        subscription.setAdCount(subscriptionDTO.getAdCount());
        subscriptionRepo.save(subscription);
        return new StandardResponse(200, "Subscription plan edited successfully", null);
    }

    @Override
    public StandardResponse deleteSubscription(Long subscriptionId) {
        SubscriptionPlanEntity subscription = this.initialSubscriptionPlanCheck(subscriptionId);
        subscriptionRepo.delete(subscription);
        return new StandardResponse(200, "Subscription plan deleted successfully", null);
    }



}
