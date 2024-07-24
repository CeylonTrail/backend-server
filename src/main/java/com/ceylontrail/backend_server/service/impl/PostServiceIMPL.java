package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.controller.AuthController;
import com.ceylontrail.backend_server.dto.post.CreatePostDTO;
import com.ceylontrail.backend_server.dto.post.DeletePostDTO;
import com.ceylontrail.backend_server.dto.post.LikePostDTO;
import com.ceylontrail.backend_server.dto.post.UpdatePostDTO;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.exception.AlreadyExistingException;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.exception.UnauthorizedException;
import com.ceylontrail.backend_server.repo.PostRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostServiceIMPL implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthController authController;

    @Override
    public PostEntity initialPostCheck(Long postId) {

        PostEntity post = postRepo.findByPostId(postId);
        if (post == null) {
            throw new NotFoundException("Post does not exist");
        }

        return post;
    }

    @Override
    public PostEntity initialPostAndUserCheck(Long postId) {

        PostEntity post = this.initialPostCheck(postId);

        UserEntity loggedUser = userRepo.findByUserId(1);
        //UserEntity loggedUser = userRepo.findByUserId(authController.getAuthenticatedId())
        if (loggedUser.getUserId() != post.getUser().getUserId()) {
            throw new UnauthorizedException("Post author is not logged in");
        }

        return post;
    }

    @Override
    public StandardResponse createPost(CreatePostDTO postDTO) {

        PostEntity post = new PostEntity();
        //post.setUser(userRepo.findByUserId(authController.getAuthenticatedId()));
        post.setUser(userRepo.findByUserId(1));
        post.setContent(postDTO.getContent());

        post = postRepo.save(post);

        Map<String, Long> postMap = new HashMap<>();
        postMap.put("postId", post.getPostId());

        return new StandardResponse(200, "Post created successfully", postMap);
    }

    @Override
    public StandardResponse updatePost(UpdatePostDTO postDTO) {

        PostEntity post = this.initialPostAndUserCheck(postDTO.getPostId());

        post.setContent(postDTO.getContent());

        postRepo.save(post);

        return new StandardResponse(200, "Post updated successfully", null);
    }

    @Override
    public StandardResponse deletePost(DeletePostDTO postDTO) {

        PostEntity post = this.initialPostAndUserCheck(postDTO.getPostId());

        postRepo.delete(post);

        return new StandardResponse(200, "Post deleted successfully", null);
    }

    @Override
    public StandardResponse addLikePost(LikePostDTO postDTO) {

        PostEntity post = this.initialPostCheck(postDTO.getPostId());
        UserEntity loggedUser = userRepo.findByUserId(1);
        //UserEntity loggedUser = userRepo.findByUserId(authController.getAuthenticatedId())

        if (post.getLikes().contains(loggedUser)) {
            throw new AlreadyExistingException("Post liked already exists");
        }

        post.getLikes().add(loggedUser);

        postRepo.save(post);

        return new StandardResponse(200, "Like added successfully", null);
    }

    @Override
    public StandardResponse removeLikePost(LikePostDTO postDTO) {

        PostEntity post = this.initialPostCheck(postDTO.getPostId());
        UserEntity loggedUser = userRepo.findByUserId(1);
        //UserEntity loggedUser = userRepo.findByUserId(authController.getAuthenticatedId())

        if (!post.getLikes().contains(loggedUser)) {
            throw new AlreadyExistingException("Post liked does not exist");
        }

        post.getLikes().remove(loggedUser);

        postRepo.save(post);

        return new StandardResponse(200, "Like removed successfully", null);
    }
}
