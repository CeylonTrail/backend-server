package com.ceylontrail.backend_server.dto.advertisement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EditAdDTO {

    private String title;
    private String description;
    private String rateType;
    private double rate;

}
