package com.ceylontrail.backend_server.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddPostDTO {

    private String content;
    private String tripId;
    private String privacy;
    private List<MultipartFile> images;

}
