package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.comment.GetCommentDTO;
import com.ceylontrail.backend_server.dto.post.*;
import com.ceylontrail.backend_server.dto.user.CommunityUserDTO;
import com.ceylontrail.backend_server.entity.ImageEntity;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.entity.enums.PostPrivacyEnum;
import com.ceylontrail.backend_server.exception.AlreadyExistingException;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.exception.UnauthorizedException;
import com.ceylontrail.backend_server.repo.PostRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.ImageService;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceIMPL implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthService authService;

    @Autowired
    private ImageService imageService;

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
        UserEntity loggedUser = userRepo.findByUserId(authService.getAuthUserId());
        if (loggedUser.getUserId() != post.getUser().getUserId()) {
            throw new UnauthorizedException("Post author is not logged in");
        }
        return post;
    }

    @Override
    public GetPostDTO postPreProcessToSend(PostEntity post) {
        UserEntity user = post.getUser();
        List<CommunityUserDTO> likes = post.getLikes().stream()
                .map(likedUser -> new CommunityUserDTO(likedUser.getUserId(), likedUser.getUsername(), likedUser.getProfilePictureUrl()))
                .collect(Collectors.toList());
        List<GetCommentDTO> comments = post.getComments().stream()
                .map(comment -> {
                    UserEntity commentUser = comment.getUser();
                    return new GetCommentDTO(
                            comment.getCommentId(),
                            new CommunityUserDTO(commentUser.getUserId(), commentUser.getUsername(), commentUser.getProfilePictureUrl()),
                            comment.getContent(),
                            comment.getCreatedAt(),
                            comment.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
        List<String> images = post.getImages().stream()
                .map(ImageEntity::getUrl).
                collect(Collectors.toList());
        return new GetPostDTO(
                post.getPostId(),
                new CommunityUserDTO(user.getUserId(), user.getUsername(), user.getProfilePictureUrl()),
                post.getContent(),
                post.getPrivacy(),
                likes,
                comments,
                images,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    @Override
    public StandardResponse getCommunityPublicPosts() {
        List<GetPostDTO> postsMap = postRepo.findPostEntitiesByPrivacyInOrderByUpdatedAtDesc(List.of(PostPrivacyEnum.PUBLIC)).stream()
                .map(this::postPreProcessToSend)
                .collect(Collectors.toList());
        return new StandardResponse(200, "Posts fetched successfully", postsMap);
    }

    @Override
    public StandardResponse getCommunityPosts() {
        // Need to randomized post order
        List<GetPostDTO> postsMap = postRepo.findAll().stream()
                .map(this::postPreProcessToSend)
                .collect(Collectors.toList());
        return new StandardResponse(200, "Posts fetched successfully", postsMap);
    }

    @Override
    public StandardResponse getUserPosts() {
        List<GetPostDTO> postsMap = postRepo.findPostEntitiesByUser_UserIdOrderByCreatedAtDesc(authService.getAuthUserId()).stream()
                .map(this::postPreProcessToSend)
                .collect(Collectors.toList());
        return new StandardResponse(200, "Posts fetched successfully", postsMap);
    }

    @Override
    public StandardResponse getPostByPostId(Long postId) {
        PostEntity post = this.initialPostCheck(postId);
        GetPostDTO postDTO = this.postPreProcessToSend(post);
        return new StandardResponse(200, "Post fetched successfully", postDTO);
    }

    @Override
    @Transactional
    public StandardResponse createPost(CreatePostDTO postDTO) {
        PostEntity post = new PostEntity();
        post.setUser(userRepo.findByUserId(authService.getAuthUserId()));
        post.setContent(postDTO.getContent());
        if (Objects.equals(postDTO.getPrivacy(), String.valueOf(PostPrivacyEnum.FOLLOWERS)))
            post.setPrivacy(PostPrivacyEnum.FOLLOWERS);
        else if (Objects.equals(postDTO.getPrivacy(), String.valueOf(PostPrivacyEnum.ONLY_ME)))
            post.setPrivacy(PostPrivacyEnum.ONLY_ME);
        else
            post.setPrivacy(PostPrivacyEnum.PUBLIC);
        post = postRepo.save(post);
        if (postDTO.getImages() != null) {
            post.setImages(imageService.UploadPostImages(post, postDTO.getImages()));
            postRepo.save(post);
        }
        Map<String, Long> postMap = new HashMap<>();
        postMap.put("postId", post.getPostId());
        return new StandardResponse(200, "Post created successfully", postMap);
    }

    @Override
    public StandardResponse updatePost(EditPostDTO postDTO) {
        PostEntity post = this.initialPostAndUserCheck(postDTO.getPostId());
        post.setContent(postDTO.getContent());
        if (Objects.equals(postDTO.getPrivacy(), String.valueOf(PostPrivacyEnum.FOLLOWERS)))
            post.setPrivacy(PostPrivacyEnum.FOLLOWERS);
        else if (Objects.equals(postDTO.getPrivacy(), String.valueOf(PostPrivacyEnum.ONLY_ME)))
            post.setPrivacy(PostPrivacyEnum.ONLY_ME);
        else
            post.setPrivacy(PostPrivacyEnum.PUBLIC);
        postRepo.save(post);
        return new StandardResponse(200, "Post updated successfully", null);
    }

    @Override
    public StandardResponse deletePost(DeletePostDTO postDTO) {
        PostEntity post = this.initialPostAndUserCheck(postDTO.getPostId());
        imageService.deletePostImages(post.getImages());
        postRepo.delete(post);
        return new StandardResponse(200, "Post deleted successfully", null);
    }

    @Override
    public StandardResponse addLikePost(LikePostDTO postDTO) {
        PostEntity post = this.initialPostCheck(postDTO.getPostId());
        UserEntity loggedUser = userRepo.findByUserId(authService.getAuthUserId());
        if (post.getLikes().contains(loggedUser))
            throw new AlreadyExistingException("Post liked already exists");
        post.getLikes().add(loggedUser);
        postRepo.save(post);
        return new StandardResponse(200, "Like added successfully", null);
    }

    @Override
    public StandardResponse removeLikePost(LikePostDTO postDTO) {
        PostEntity post = this.initialPostCheck(postDTO.getPostId());
        UserEntity loggedUser = userRepo.findByUserId(authService.getAuthUserId());
        if (!post.getLikes().contains(loggedUser))
            throw new AlreadyExistingException("Post liked does not exist");
        post.getLikes().remove(loggedUser);
        postRepo.save(post);
        return new StandardResponse(200, "Like removed successfully", null);
    }

}
