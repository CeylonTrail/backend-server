package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.post.AddPostDTO;
import com.ceylontrail.backend_server.dto.post.EditPostDTO;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("api/v1/post")
public class PostController {

    private final PostService postService;

    @GetMapping(path = "/community-feed")
    public StandardResponse getCommunityPosts() {
        return postService.getCommunityPosts();
    }

    @GetMapping(path = "/community-feed/public")
    public StandardResponse getCommunityPublicPosts() {
        return postService.getCommunityPublicPosts();
    }

    @GetMapping("/{postId}")
    public StandardResponse getPostByPostId(@PathVariable Long postId) {
        return postService.getPostByPostId(postId);
    }

    @PostMapping
    public StandardResponse createPost(@ModelAttribute AddPostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PutMapping("/{postId}")
    public StandardResponse updatePost(@PathVariable Long postId, @RequestBody EditPostDTO postDTO) {
        return postService.updatePost(postId, postDTO);
    }

    @DeleteMapping("/{postId}")
    public StandardResponse deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }

    @PutMapping(path = "/add-like/{postId}")
    public StandardResponse addLikePost(@PathVariable Long postId) {
        return postService.addLike(postId);
    }

    @PutMapping(path = "/remove-like/{postId}")
    public StandardResponse removeLikePost(@PathVariable Long postId) {
        return postService.removeLike(postId);
    }
}
