package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.comment.AddCommentDTO;
import com.ceylontrail.backend_server.dto.comment.RemoveCommentDTO;
import com.ceylontrail.backend_server.dto.comment.UpdateCommentDTO;
import com.ceylontrail.backend_server.service.CommentService;
import com.ceylontrail.backend_server.util.StandardResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping()
    public StandardResponse addComment(@Valid @RequestBody AddCommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }

    @PutMapping()
    public StandardResponse updateComment(@Valid @RequestBody UpdateCommentDTO commentDTO) {
        return commentService.updateComment(commentDTO);
    }

    @DeleteMapping()
    public StandardResponse removeComment(@Valid @RequestBody RemoveCommentDTO commentDTO) {
        return commentService.removeComment(commentDTO);
    }

}
