package com.ceylontrail.backend_server.dto.post;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Privacy Type is reuire")
    private String privacy;
    private List<MultipartFile> images;

}
