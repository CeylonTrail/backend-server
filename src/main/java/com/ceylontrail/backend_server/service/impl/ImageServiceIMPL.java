package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.entity.ImageEntity;
import com.ceylontrail.backend_server.entity.PostEntity;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.repo.ImageRepo;
import com.ceylontrail.backend_server.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceIMPL implements ImageService {

    @Autowired
    private ImageRepo imageRepo;

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${server.url}")
    private String serverUrl;

    @Override
    public String generateUniqueFilename(MultipartFile file) {
        String fileName;
        String fileExtension = getFileExtension(file.getOriginalFilename());
        do {
            fileName = UUID.randomUUID() + fileExtension;
        } while (imageRepo.existsByFilename(fileName));
        return fileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        } else {
            return "";
        }
    }

    public ImageEntity uploadImage(MultipartFile file){
        String filename = this.generateUniqueFilename(file);
        Path filepath = Paths.get(uploadDir, filename);
        try {
            Files.copy(file.getInputStream(), filepath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
        ImageEntity image = new ImageEntity();
        image.setFilename(filename);
        image.setUrl(serverUrl + "/api/v1/image/" + filename);
        return imageRepo.save(image);
    }

    public void deleteImage(ImageEntity image) {
        String filename = image.getFilename();
        Path filepath = Paths.get(uploadDir, filename);
        if (Files.exists(filepath))
            try {
                Files.delete(filepath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to remove image", e);
            }
        if (imageRepo.existsByFilename(filename)) {
            imageRepo.delete(image);
        }
    }

    @Override
    public List<ImageEntity> UploadPostImages(PostEntity post, List<MultipartFile> files) {
        return files.stream().map(file -> {
            ImageEntity image = this.uploadImage(file);
            image.setPost(post);
            return imageRepo.save(image);
        }).collect(Collectors.toList());
    }

    @Override
    public void deletePostImages(List<ImageEntity> images) {
        for (ImageEntity image : images) {
            deleteImage(image);
        }
    }

    @Override
    public Resource getImageResource(String filename){
        if (!imageRepo.existsByFilename(filename))
            throw new NotFoundException("Image does not exist");
        ImageEntity image = imageRepo.findByFilename(filename);
        Path filepath = Paths.get(uploadDir, image.getFilename());
        if (Files.notExists(filepath))
            throw new NotFoundException("Image not found on the server");
        try {
            return new UrlResource(filepath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load image", e);
        }
    }

}
