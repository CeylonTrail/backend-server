package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("api/v1/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource resource = imageService.getImageResource(filename);
        String fileType = Objects.requireNonNull(resource.getFilename()).substring(resource.getFilename().lastIndexOf(".") + 1);
        if (resource.exists() && resource.isReadable()) {
            String contentType;
            if (fileType.equals("jpg")) {
                contentType = "image/jpeg";
            } else if (fileType.equals("png")) {
                contentType = "image/png";
            } else {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            throw new NotFoundException("Image does not exist");
        }
    }
}
         