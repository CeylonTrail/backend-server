package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.post.*;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface PostService {

    PostEntity initialPostCheck(Long postId);

    PostEntity initialPostAndUserCheck(Long postId);

    GetPostDTO postPreProcessToSend(PostEntity post);

    StandardResponse getUserPosts();

    StandardResponse getCommunityPosts();

    StandardResponse getCommunityPublicPosts();

    StandardResponse getPostByPostId(Long postId);

    StandardResponse createPost(CreatePostDTO postDTO);

    StandardResponse updatePost(EditPostDTO postDTO);

    StandardResponse deletePost(DeletePostDTO postDTO);

    StandardResponse addLikePost(LikePostDTO postDTO);

    StandardResponse removeLikePost(LikePostDTO postDTO);

}
