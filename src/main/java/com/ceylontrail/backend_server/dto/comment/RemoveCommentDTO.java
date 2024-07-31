package com.ceylontrail.backend_server.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveCommentDTO {

    @NotNull(message = "Comment Id is required!")
    private Long commentId;

}
