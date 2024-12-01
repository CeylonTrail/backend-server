package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.util.StandardResponse;

public interface AdminService {
    StandardResponse getTravellers(String search, int pageNumber, int pageSize);

    StandardResponse getTraveller(int userId);

    StandardResponse deleteTraveller(int userId);

    StandardResponse getSPs(String key, int pageNumber, int pageSize);

    StandardResponse getSP(int userId);

    StandardResponse deleteSP(int userId);

    StandardResponse deletePost(Long postId);
}
