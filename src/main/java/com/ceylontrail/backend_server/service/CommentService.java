package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.comment.AddCommentDTO;
import com.ceylontrail.backend_server.dto.comment.RemoveCommentDTO;
import com.ceylontrail.backend_server.dto.comment.UpdateCommentDTO;
import com.ceylontrail.backend_server.entity.CommentEntity;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface CommentService {
    CommentEntity initialCommentAndUserCheck(Long commentId);

    StandardResponse addComment(AddCommentDTO commentDTO);
    StandardResponse updateComment(UpdateCommentDTO commentDTO);
    StandardResponse removeComment(RemoveCommentDTO commentDTO);
}
