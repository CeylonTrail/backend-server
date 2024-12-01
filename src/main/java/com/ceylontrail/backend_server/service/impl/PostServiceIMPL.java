package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.comment.GetCommentDTO;
import com.ceylontrail.backend_server.dto.post.*;
import com.ceylontrail.backend_server.dto.trip.CommunityTripDTO;
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
import com.ceylontrail.backend_server.service.TripService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceIMPL implements PostService {

    private final AuthService authService;
    private final ImageService imageService;
    private final TripService tripService;

    private final PostRepo postRepo;
    private final UserRepo userRepo;

    @Override
    public PostEntity initialPostCheck(Long postId) {
        PostEntity post = postRepo.findByPostId(postId);
        if (post == null) {
            throw new NotFoundException("Post does not exist");
        }
        return post;
    }

    private PostEntity initialPostAndUserCheck(Long postId) {
        PostEntity post = this.initialPostCheck(postId);
        UserEntity loggedUser = userRepo.findByUserId(authService.getAuthUserId());
        if (loggedUser.getUserId() != post.getUser().getUserId()) {
            throw new UnauthorizedException("Post author is not logged in");
        }
        return post;
    }

    private GetPostFeedDTO postPreProcessForSendToFeed(PostEntity post) {
        return new GetPostFeedDTO(
                post.getPostId(),
                new CommunityUserDTO(
                        post.getUser().getUserId(),
                        post.getUser().getUsername(),
                        post.getUser().getProfilePictureUrl()
                ),
                post.getContent(),
                (post.getTrip() != null) ? new CommunityTripDTO(
                        post.getTrip().getTripId(),
                        post.getTrip().getDestination(),
                        post.getTrip().getDayCount()
                ) : null,
                post.getPrivacy(),
                post.getLikes().size(),
                post.getComments().size(),
                (post.getImages() != null && !post.getImages().isEmpty()) ?
                        post.getImages().stream().map(ImageEntity::getUrl).toList() :
                        List.of(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }


    @Override
    public StandardResponse getCommunityPublicPosts() {
        List<GetPostFeedDTO> postsMap = postRepo.findPostEntitiesByPrivacyInOrderByUpdatedAtDesc(List.of(PostPrivacyEnum.PUBLIC)).stream()
                .map(this::postPreProcessForSendToFeed)
                .collect(Collectors.toList());
        return new StandardResponse(200, "Posts fetched successfully", postsMap);
    }

    @Override
    public StandardResponse getCommunityPosts() {
        // Need to randomized post order
        List<GetPostFeedDTO> postsMap = postRepo.findAll().stream()
                .map(this::postPreProcessForSendToFeed)
                .collect(Collectors.toList());
        return new StandardResponse(200, "Posts fetched successfully", postsMap);
    }

    @Override
    public List<GetPostFeedDTO> getUserPosts(int userId) {
        return postRepo.findPostEntitiesByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::postPreProcessForSendToFeed)
                .toList();
    }

    @Override
    public StandardResponse getPostByPostId(Long postId) {
        PostEntity post = this.initialPostCheck(postId);
        GetPostDTO postDTO = new GetPostDTO();
        postDTO.setPostId(post.getPostId());
        postDTO.setUser(new CommunityUserDTO(
                post.getUser().getUserId(),
                post.getUser().getUsername(),
                post.getUser().getProfilePictureUrl())
        );
        postDTO.setContent(post.getContent());
        postDTO.setPrivacy(post.getPrivacy());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        if (post.getTrip() != null) {
            postDTO.setTrip(new CommunityTripDTO(
                    post.getTrip().getTripId(),
                    post.getTrip().getDestination(),
                    post.getTrip().getDayCount())
            );
        } else {
            postDTO.setTrip(null);
        }
        postDTO.setLikes(post.getLikes().stream()
                .map(likedUser -> new CommunityUserDTO(
                        likedUser.getUserId(),
                        likedUser.getUsername(),
                        likedUser.getProfilePictureUrl())
                ).toList()
        );
        postDTO.setComments(post.getComments().stream()
                .map(comment -> {
                    UserEntity commentUser = comment.getUser();
                    return new GetCommentDTO(
                            comment.getCommentId(),
                            new CommunityUserDTO(
                                    commentUser.getUserId(),
                                    commentUser.getUsername(),
                                    commentUser.getProfilePictureUrl()
                            ),
                            comment.getContent(),
                            comment.getCreatedAt(),
                            comment.getUpdatedAt()
                    );
                }).toList()
        );
        postDTO.setImages(post.getImages().stream()
                .map(ImageEntity::getUrl)
                .toList()
        );
        return new StandardResponse(200, "Post fetched successfully", postDTO);
    }

    @Override
    @Transactional
    public StandardResponse createPost(AddPostDTO postDTO) {
        PostEntity post = new PostEntity();
        post.setUser(userRepo.findByUserId(authService.getAuthUserId()));
        post.setContent(postDTO.getContent());
        if (postDTO.getTripId() != null)
            post.setTrip(this.tripService.initialTripAndUserCheck(Integer.parseInt(postDTO.getTripId())));
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
        return new StandardResponse(200, "Post created successfully", null);
    }

    @Override
    public StandardResponse updatePost(Long postId, EditPostDTO postDTO) {
        PostEntity post = this.initialPostAndUserCheck(postId);
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
    public StandardResponse deletePost(Long postId) {
        PostEntity post = this.initialPostAndUserCheck(postId);
        imageService.deletePostImages(post.getImages());
        postRepo.delete(post);
        return new StandardResponse(200, "Post deleted successfully", null);
    }

    @Override
    public StandardResponse addLike(Long postId) {
        PostEntity post = this.initialPostCheck(postId);
        UserEntity loggedUser = userRepo.findByUserId(authService.getAuthUserId());
        if (post.getLikes().contains(loggedUser))
            throw new AlreadyExistingException("Post liked already exists");
        post.getLikes().add(loggedUser);
        postRepo.save(post);
        return new StandardResponse(200, "Like added successfully", null);
    }

    @Override
    public StandardResponse removeLike(Long postId) {
        PostEntity post = this.initialPostCheck(postId);
        UserEntity loggedUser = userRepo.findByUserId(authService.getAuthUserId());
        if (!post.getLikes().contains(loggedUser))
            throw new AlreadyExistingException("Post liked does not exist");
        post.getLikes().remove(loggedUser);
        postRepo.save(post);
        return new StandardResponse(200, "Like removed successfully", null);
    }

}
