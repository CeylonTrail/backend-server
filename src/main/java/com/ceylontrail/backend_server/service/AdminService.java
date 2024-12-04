package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.admin.UpdateVerificationDTO;
import com.ceylontrail.backend_server.dto.subscription.AddSubscriptionDTO;
import com.ceylontrail.backend_server.dto.subscription.EditSubscriptionDTO;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface AdminService {
    StandardResponse getDashboard();

    StandardResponse getTravellers();

    StandardResponse deleteTraveller(int userId);

    StandardResponse getSPs();

    StandardResponse getSP(Long spId);

    StandardResponse deleteSP(int userId);

    StandardResponse getAllVerificationSPs();

    StandardResponse getVerificationSP(Long spId);

    StandardResponse deletePost(Long postId);

    StandardResponse addSubscription(AddSubscriptionDTO subscriptionDTO);

    StandardResponse getSubscription(Long subscriptionId);

    StandardResponse editSubscription(Long subscriptionId, EditSubscriptionDTO subscriptionDTO);

    StandardResponse deleteSubscription(Long subscriptionId);

    StandardResponse updateVerificationSP(Long spId, UpdateVerificationDTO updateVerificationDTO);
}
