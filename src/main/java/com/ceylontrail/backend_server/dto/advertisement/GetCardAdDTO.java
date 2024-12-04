package com.ceylontrail.backend_server.dto.advertisement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetCardAdDTO {

    private Long id;
    private String title;
    private String rateType;
    private double rate;
    private String isActive;
    private List<String> images;

}
