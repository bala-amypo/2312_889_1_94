package com.example.demo.controller;

import com.example.demo.model.Zone;
import com.example.demo.service.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {
    
    private final ZoneService zoneService;
    
    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }
    
    @PostMapping
    public ResponseEntity<Zone> createZone(@RequestBody Zone zone) {
        Zone created = zoneService.createZone(zone);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Zone> updateZone(@PathVariable Long id, @RequestBody Zone zone) {
        Zone updated = zoneService.updateZone(id, zone);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZone(@PathVariable Long id) {
        Zone zone = zoneService.getZoneById(id);
        return ResponseEntity.ok(zone);
    }
    
    @GetMapping
    public ResponseEntity<List<Zone>> getAllZones() {
        List<Zone> zones = zoneService.getAllZones();
        return ResponseEntity.ok(zones);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateZone(@PathVariable Long id) {
        zoneService.deactivateZone(id);
        return ResponseEntity.noContent().build();
    }
}