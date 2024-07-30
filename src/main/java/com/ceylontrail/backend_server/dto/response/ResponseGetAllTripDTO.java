package com.ceylontrail.backend_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class ResponseGetAllTripDTO {
    private int tripId;
    private String destination;
    private int dayCount;
    private String description;
    private LocalDate createdAt;
    private LocalDate updateAt;
}
