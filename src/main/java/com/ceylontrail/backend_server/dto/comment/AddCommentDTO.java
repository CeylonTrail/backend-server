package com.ceylontrail.backend_server.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentDTO {

    private Long postId;
    private String content;

}
