package com.ceylontrail.backend_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialMediaLinks {
    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String link;
}
