package com.ceylontrail.backend_server.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePostDTO {

    private String content;
    private int tripId;

}
