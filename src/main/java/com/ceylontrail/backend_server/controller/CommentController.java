package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.comment.AddCommentDTO;
import com.ceylontrail.backend_server.dto.comment.EditCommentDTO;
import com.ceylontrail.backend_server.service.CommentService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public StandardResponse addComment(@RequestBody AddCommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }

    @PutMapping("/{commentId}")
    public StandardResponse updateComment(@PathVariable Long commentId, @RequestBody EditCommentDTO commentDTO) {
        return commentService.updateComment(commentId, commentDTO);
    }

    @DeleteMapping("/{commentId}")
    public StandardResponse removeComment(@PathVariable Long commentId) {
        return commentService.removeComment(commentId);
    }

}
