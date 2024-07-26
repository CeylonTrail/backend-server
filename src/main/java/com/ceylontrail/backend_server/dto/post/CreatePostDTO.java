package com.ceylontrail.backend_server.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePostDTO {

    private String content;
    private int tripId;
    private List<MultipartFile> images;

}
