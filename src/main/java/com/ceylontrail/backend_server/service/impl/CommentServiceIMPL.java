package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.comment.AddCommentDTO;
import com.ceylontrail.backend_server.dto.comment.EditCommentDTO;
import com.ceylontrail.backend_server.entity.CommentEntity;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.exception.UnauthorizedException;
import com.ceylontrail.backend_server.repo.CommentRepo;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.CommentService;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentServiceIMPL implements CommentService {

    private final AuthService authService;
    private final PostService postService;

    private final UserRepo userRepo;
    private final CommentRepo commentRepo;

    private CommentEntity initialCommentAndUserCheck(Long commentId) {
        CommentEntity comment = commentRepo.findByCommentId(commentId);
        if (comment == null)
            throw new NotFoundException("Comment does not exist");
        if (userRepo.findByUserId(authService.getAuthUserId()).getUserId() != comment.getUser().getUserId())
            throw new UnauthorizedException("Comment author is not logged in");
        return comment;
    }

    @Override
    public StandardResponse addComment(AddCommentDTO commentDTO) {
        CommentEntity comment = new CommentEntity();
        comment.setPost(postService.initialPostCheck(commentDTO.getPostId()));
        comment.setUser(userRepo.findByUserId(authService.getAuthUserId()));
        comment.setContent(commentDTO.getContent());
        commentRepo.save(comment);
        return new StandardResponse(200, "Comment added successfully", null);
    }

    @Override
    public StandardResponse updateComment(Long commentId, EditCommentDTO commentDTO) {
        CommentEntity comment = this.initialCommentAndUserCheck(commentId);
        comment.setContent(commentDTO.getContent());
        commentRepo.save(comment);
        return new StandardResponse(200, "Comment updated successfully", null);
    }

    @Override
    public StandardResponse removeComment(Long commentId) {
        CommentEntity comment = this.initialCommentAndUserCheck(commentId);
        commentRepo.delete(comment);
        return new StandardResponse(200, "Comment deleted successfully", null);
    }
}
