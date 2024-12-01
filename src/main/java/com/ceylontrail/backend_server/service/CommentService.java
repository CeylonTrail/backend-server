package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.dto.comment.AddCommentDTO;
import com.ceylontrail.backend_server.dto.comment.EditCommentDTO;
import com.ceylontrail.backend_server.util.StandardResponse;

public interface CommentService {
    StandardResponse addComment(AddCommentDTO commentDTO);

    StandardResponse updateComment(Long commentId, EditCommentDTO commentDTO);

    StandardResponse removeComment(Long commentId);
}
