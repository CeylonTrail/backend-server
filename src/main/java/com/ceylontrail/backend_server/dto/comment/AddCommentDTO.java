package com.ceylontrail.backend_server.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentDTO {

    @NotBlank(message = "Post Id is required!")
    private Long postId;

    @NotBlank(message = "Content is required!")
    private String content;

}
