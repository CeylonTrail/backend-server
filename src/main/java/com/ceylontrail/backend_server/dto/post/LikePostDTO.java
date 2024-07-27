package com.ceylontrail.backend_server.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LikePostDTO {

    @NotNull(message = "Post Id is required!")
    private Long postId;

}
