package com.ceylontrail.backend_server.dto.advertisement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAdDTO {

    private Long advertisementId;
    private String title;
    private String description;
    private String rateType;
    private double rate;
    private String isActive;
    private String createdAt;
    private List<String> images;

}
