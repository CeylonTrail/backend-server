package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.post.CreatePostDTO;
import com.ceylontrail.backend_server.dto.post.DeletePostDTO;
import com.ceylontrail.backend_server.dto.post.LikePostDTO;
import com.ceylontrail.backend_server.dto.post.UpdatePostDTO;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface PostService {
    PostEntity initialPostCheck(Long postId);
    PostEntity initialPostAndUserCheck(Long postId);

    StandardResponse createPost(CreatePostDTO postDTO);
    StandardResponse updatePost(UpdatePostDTO postDTO);
    StandardResponse deletePost(DeletePostDTO postDTO);
    StandardResponse addLikePost(LikePostDTO postDTO);
    StandardResponse removeLikePost(LikePostDTO postDTO);
}
