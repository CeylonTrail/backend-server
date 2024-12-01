package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.subscription.AddSubscriptionDTO;
import com.ceylontrail.backend_server.dto.subscription.EditSubscriptionDTO;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface AdminService {
    StandardResponse getTravellers(String search, int pageNumber, int pageSize);

    StandardResponse getTraveller(int userId);

    StandardResponse deleteTraveller(int userId);

    StandardResponse getSPs(String key, int pageNumber, int pageSize);

    StandardResponse getSP(int userId);

    StandardResponse deleteSP(int userId);

    StandardResponse deletePost(Long postId);

    StandardResponse addSubscription(AddSubscriptionDTO subscriptionDTO);

    StandardResponse getSubscription(Long subscriptionId);

    StandardResponse editSubscription(Long subscriptionId, EditSubscriptionDTO subscriptionDTO);

    StandardResponse deleteSubscription(Long subscriptionId);
}
