package com.ceylontrail.backend_server.dto.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialMediaLinkDTO {
    private String socialMedia;
    private String url;
}
