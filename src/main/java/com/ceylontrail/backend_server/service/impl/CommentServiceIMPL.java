package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.controller.AuthController;
import com.ceylontrail.backend_server.dto.comment.AddCommentDTO;
import com.ceylontrail.backend_server.dto.comment.RemoveCommentDTO;
import com.ceylontrail.backend_server.dto.comment.UpdateCommentDTO;
import com.ceylontrail.backend_server.entity.CommentEntity;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.exception.UnauthorizedException;
import com.ceylontrail.backend_server.repo.CommentRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.CommentService;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommentServiceIMPL implements CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostService postService;

    @Autowired
    private AuthController authController;

    @Override
    public CommentEntity initialCommentAndUserCheck(Long commentId) {

        CommentEntity comment = commentRepo.findByCommentId(commentId);
        if (comment == null) {
            throw new NotFoundException("Comment does not exist");
        }

        UserEntity loggedUser = userRepo.findByUserId(1);
        //UserEntity loggedUser = userRepo.findByUserId(authController.getAuthenticatedId())
        if (loggedUser.getUserId() != comment.getUser().getUserId()) {
            throw new UnauthorizedException("Comment author is not logged in");
        }

        return comment;
    }


    @Override
    public StandardResponse addComment(AddCommentDTO commentDTO) {

        PostEntity post = postService.initialPostCheck(commentDTO.getPostId());
        // UserEntity user = userRepo.findByUserId(authController.getAuthenticatedId());
        UserEntity user = userRepo.findByUserId(1);

        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDTO.getContent());

        CommentEntity savedComment = commentRepo.save(comment);

        Map<String, Long> commentMap = new HashMap<>();
        commentMap.put("commentId", savedComment.getCommentId());

        return new StandardResponse(200, "Comment added successfully", commentMap);
    }

    @Override
    public StandardResponse updateComment(UpdateCommentDTO commentDTO) {

        CommentEntity comment = this.initialCommentAndUserCheck(commentDTO.getCommentId());

        comment.setContent(commentDTO.getContent());

        commentRepo.save(comment);

        return new StandardResponse(200, "Comment updated successfully", null);
    }

    @Override
    public StandardResponse removeComment(RemoveCommentDTO commentDTO) {

        CommentEntity comment = this.initialCommentAndUserCheck(commentDTO.getCommentId());

        commentRepo.delete(comment);

        return new StandardResponse(200, "Comment deleted successfully", null);
    }
}
