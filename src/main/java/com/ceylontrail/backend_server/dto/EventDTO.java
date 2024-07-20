package com.ceylontrail.backend_server.dto;

import com.ceylontrail.backend_server.entity.TripEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventDTO {

    private int trip;
    private String description;
    private int dayNum;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
