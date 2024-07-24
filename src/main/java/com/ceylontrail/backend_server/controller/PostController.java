package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.post.CreatePostDTO;
import com.ceylontrail.backend_server.dto.post.DeletePostDTO;
import com.ceylontrail.backend_server.dto.post.LikePostDTO;
import com.ceylontrail.backend_server.dto.post.UpdatePostDTO;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.util.StandardResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public StandardResponse createPost(@Valid @RequestBody CreatePostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PutMapping
    public StandardResponse updatePost(@Valid @RequestBody UpdatePostDTO postDTO) {
        return postService.updatePost(postDTO);
    }

    @DeleteMapping
    public StandardResponse deletePost(@Valid @RequestBody DeletePostDTO postDTO) {
        return postService.deletePost(postDTO);
    }

    @PostMapping(path = "/like")
    public StandardResponse addLikePost(@Valid @RequestBody LikePostDTO postDTO) {
        return postService.addLikePost(postDTO);
    }

    @DeleteMapping(path = "/like")
    public StandardResponse removeLikePost(@Valid @RequestBody LikePostDTO postDTO) {
        return postService.removeLikePost(postDTO);
    }
}

