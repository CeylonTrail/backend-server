package com.ceylontrail.backend_server.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeletePostDTO {

    @NotBlank(message = "Post Id is required!")
    private Long postId;

}
