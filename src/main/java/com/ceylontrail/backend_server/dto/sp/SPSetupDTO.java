package com.ceylontrail.backend_server.dto.sp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SPSetupDTO {

    private MultipartFile profilePicture;
    private MultipartFile coverPicture;
    private String description;
    private String contactNumber;
    private String address;
    private MultipartFile verificationDoc;
    private List<SocialMediaLinksDTO> socialMediaLinks;
    private List<OpeningHoursDTO> openingHours;

}
