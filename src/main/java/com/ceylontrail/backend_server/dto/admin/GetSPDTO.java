package com.ceylontrail.backend_server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSPDTO {

    private int pageNumber;

    private int totalPages;

    private int totalElements;

    private int pageSize;

    private List<SP> SPs = new ArrayList<>();

}
