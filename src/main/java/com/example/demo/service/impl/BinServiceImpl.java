package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Bin;
import com.example.demo.model.Zone;
import com.example.demo.repository.BinRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.BinService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BinServiceImpl implements BinService {
    private final BinRepository binRepository;
    private final ZoneRepository zoneRepository;

    public BinServiceImpl(BinRepository binRepository, ZoneRepository zoneRepository) {
        this.binRepository = binRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Bin createBin(Bin bin) {
        if (bin.getCapacityLiters() == null || bin.getCapacityLiters() <= 0) {
            throw new BadRequestException("Bin capacity must be greater than 0");
        }
        
        if (bin.getZone() != null && bin.getZone().getId() != null) {
            Zone zone = zoneRepository.findById(bin.getZone().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
            if (!zone.getActive()) {
                throw new BadRequestException("Cannot create bin in inactive zone");
            }
            bin.setZone(zone);
        }
        
        if (bin.getActive() == null) {
            bin.setActive(true);
        }
        
        LocalDateTime now = LocalDateTime.now();
        bin.setCreatedAt(now);
        bin.setUpdatedAt(now);
        
        return binRepository.save(bin);
    }

    @Override
    public Bin updateBin(Long id, Bin bin) {
        Bin existing = getBinById(id);
        existing.setLocationDescription(bin.getLocationDescription());
        existing.setLatitude(bin.getLatitude());
        existing.setLongitude(bin.getLongitude());
        
        if (bin.getCapacityLiters() != null) {
            if (bin.getCapacityLiters() <= 0) {
                throw new BadRequestException("Bin capacity must be greater than 0");
            }
            existing.setCapacityLiters(bin.getCapacityLiters());
        }
        
        existing.setUpdatedAt(LocalDateTime.now());
        return binRepository.save(existing);
    }

    @Override
    public Bin getBinById(Long id) {
        return binRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bin not found"));
    }

    @Override
    public List<Bin> getAllBins() {
        return binRepository.findAll();
    }

    @Override
    public void deactivateBin(Long id) {
        Bin bin = getBinById(id);
        bin.setActive(false);
        bin.setUpdatedAt(LocalDateTime.now());
        binRepository.save(bin);
    }
}