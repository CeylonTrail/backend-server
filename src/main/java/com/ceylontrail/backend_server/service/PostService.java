package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.post.*;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.util.StandardResponse;

import java.util.List;

public interface PostService {
    PostEntity initialPostCheck(Long postId);

    List<GetPostFeedDTO> getUserPosts(int userId);

    StandardResponse getCommunityPosts();

    StandardResponse getCommunityPublicPosts();

    StandardResponse getPostByPostId(Long postId);

    StandardResponse createPost(AddPostDTO postDTO);

    StandardResponse updatePost(Long postId, EditPostDTO postDTO);

    StandardResponse deletePost(Long postId);

    StandardResponse addLike(Long postId);

    StandardResponse removeLike(Long postId);

    StandardResponse addComment(AddCommentDTO commentDTO);

    StandardResponse updateComment(Long commentId, EditCommentDTO commentDTO);

    StandardResponse removeComment(Long commentId);

    StandardResponse reportPost(Long postId, ReportPostDTO postDTO);
}
