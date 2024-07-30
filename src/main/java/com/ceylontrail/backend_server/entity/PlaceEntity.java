package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "place")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaceEntity {
    @Id
    @Column(name = "place_id" ,nullable = false)
    private String placeId;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "latitude",columnDefinition = "DOUBLE PRECISION")
    private double latitude;
    @Column(name = "longitude",columnDefinition = "DOUBLE PRECISION")
    private double longitude;
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    @Column(name = "photo_url" ,nullable = false, length = 1000)
    private String photoUrl;
    @Column(name = "rating",columnDefinition = "DOUBLE PRECISION")
    private double rating;

    public PlaceEntity(String placeId, String name, double latitude, double longitude, String description, String photoUrl, double rating) {
        this.placeId = placeId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.photoUrl = photoUrl;
        this.rating = rating;
    }
}
