package com.ceylontrail.backend_server.dto.requests;

import com.ceylontrail.backend_server.dto.EventDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class RequestTripSaveDTO {
    private int tripId;
    private String destination;
    private int dayCount;
    private String description;
    private LocalDate createdAt;
    private LocalDate updateAt;
    private List<EventDTO> eventSet;
}
