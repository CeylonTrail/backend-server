package com.ceylontrail.backend_server.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditPostDTO {

    private String content;
    private String privacy;

}
