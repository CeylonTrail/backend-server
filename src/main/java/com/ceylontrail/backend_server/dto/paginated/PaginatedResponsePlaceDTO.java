package com.ceylontrail.backend_server.dto.paginated;
import com.ceylontrail.backend_server.dto.places.PlaceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedResponsePlaceDTO {
    private List<PlaceDTO> places;
    private long dataCount;
}
