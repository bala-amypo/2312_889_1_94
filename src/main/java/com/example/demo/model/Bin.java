package com.example.demo.model;

import java.time.Instant;

public class Bin {

    private Long id;
    private String identifier;
    private String locationDescription;
    private Double latitude;
    private Double longitude;
    private Zone zone;
    private Double capacityLiters;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public Bin() {
        this.active = true; // default
    }

    public Bin(Long id, String identifier, String locationDescription, Double latitude, Double longitude,
               Zone zone, Double capacityLiters, Boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.identifier = identifier;
        this.locationDescription = locationDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zone = zone;
        this.capacityLiters = capacityLiters;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
