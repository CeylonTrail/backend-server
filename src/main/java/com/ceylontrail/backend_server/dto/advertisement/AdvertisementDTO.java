package com.ceylontrail.backend_server.dto.advertisement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdvertisementDTO {

    private String title;
    private String description;
    private String rateType;
    private double rate;
    private List<MultipartFile> images;

}
