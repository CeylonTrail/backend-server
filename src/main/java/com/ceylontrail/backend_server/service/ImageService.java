package com.ceylontrail.backend_server.service;

import com.ceylontrail.backend_server.entity.ImageEntity;
import com.ceylontrail.backend_server.entity.PostEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ImageEntity uploadImage(MultipartFile file);

    void deleteImage(ImageEntity image);

    List<ImageEntity> UploadPostImages(PostEntity post, List<MultipartFile> images);

    void deletePostImages(List<ImageEntity> images);

    Resource getImageResource(String filename);
}
