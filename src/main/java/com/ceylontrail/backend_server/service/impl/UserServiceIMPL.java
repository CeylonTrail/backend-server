package com.ceylontrail.backend_server.service.impl;

import com.ceylontrail.backend_server.dto.user.TravellerProfileDTO;
import com.ceylontrail.backend_server.entity.UserEntity;
import com.ceylontrail.backend_server.exception.NotFoundException;
import com.ceylontrail.backend_server.repo.UserRepo;
import com.ceylontrail.backend_server.service.AuthService;
import com.ceylontrail.backend_server.service.ImageService;
import com.ceylontrail.backend_server.service.PostService;
import com.ceylontrail.backend_server.service.UserService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceIMPL implements UserService {

    private final AuthService authService;
    private final PostService postService;

    private final UserRepo userRepo;
    private final ImageService imageService;

    @Override
    public UserEntity initialUserCheck(int userId) {
        UserEntity user = userRepo.findByUserId(userId);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }
        return user;
    }

    @Override
    public StandardResponse getUserProfile() {
        UserEntity user = userRepo.findByUserId(authService.getAuthUserId());
        TravellerProfileDTO profileDTO = new TravellerProfileDTO();
        profileDTO.setUserId(user.getUserId());
        profileDTO.setFirstname(user.getFirstname());
        profileDTO.setLastname(user.getLastname());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setUsername(user.getUsername());
        profileDTO.setProfilePictureUrl(user.getProfilePictureUrl());
        profileDTO.setCreatedAt(user.getCreatedAt());
        profileDTO.setUpdatedAt(user.getUpdatedAt());
        profileDTO.setPosts(postService.getUserPosts(user.getUserId()));
        profileDTO.setImages(imageService.getImageUrlsByUserId(user.getUserId()));
        return new StandardResponse(200, "Profile fetched successfully", profileDTO);
    }

}
