package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.post.AddCommentDTO;
import com.ceylontrail.backend_server.dto.post.EditCommentDTO;
import com.ceylontrail.backend_server.dto.post.AddPostDTO;
import com.ceylontrail.backend_server.dto.post.EditPostDTO;
import com.ceylontrail.backend_server.dto.post.ReportPostDTO;
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

    @PostMapping("/comment")
    public StandardResponse addComment(@RequestBody AddCommentDTO commentDTO) {
        return postService.addComment(commentDTO);
    }

    @PutMapping("/comment/{commentId}")
    public StandardResponse updateComment(@PathVariable Long commentId, @RequestBody EditCommentDTO commentDTO) {
        return postService.updateComment(commentId, commentDTO);
    }

    @DeleteMapping("/comment/{commentId}")
    public StandardResponse removeComment(@PathVariable Long commentId) {
        return postService.removeComment(commentId);
    }

    @PostMapping("report/{postId}")
    public StandardResponse reportPost(@PathVariable Long postId, @RequestBody ReportPostDTO postDTO) {
        return postService.reportPost(postId, postDTO);
    }
}
