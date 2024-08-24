package com.ceylontrail.backend_server.dto.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OpeningHoursDTO {
    private String day;
    private String startTime;
    private String endTime;
}
