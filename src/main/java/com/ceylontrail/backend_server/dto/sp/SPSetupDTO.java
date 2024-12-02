package com.ceylontrail.backend_server.dto.sp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private String socialMediaLinks;
    private String openingHours;

}
