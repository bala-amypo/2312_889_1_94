package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.OverflowPredictionService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OverflowPredictionServiceImpl implements OverflowPredictionService {
    private final BinRepository binRepository;
    private final FillLevelRecordRepository recordRepository;
    private final UsagePatternModelRepository modelRepository;
    private final OverflowPredictionRepository predictionRepository;
    private final ZoneRepository zoneRepository;

    public OverflowPredictionServiceImpl(BinRepository binRepository, FillLevelRecordRepository recordRepository, 
                                       UsagePatternModelRepository modelRepository, OverflowPredictionRepository predictionRepository,
                                       ZoneRepository zoneRepository) {
        this.binRepository = binRepository;
        this.recordRepository = recordRepository;
        this.modelRepository = modelRepository;
        this.predictionRepository = predictionRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public OverflowPrediction generatePrediction(Long binId) {
        Bin bin = binRepository.findById(binId)
                .orElseThrow(() -> new ResourceNotFoundException("Bin not found"));
        
        FillLevelRecord latestRecord = recordRepository.findTop1ByBinOrderByRecordedAtDesc(bin)
                .orElseThrow(() -> new ResourceNotFoundException("No fill records found for bin"));
        
        UsagePatternModel model = modelRepository.findTop1ByBinOrderByLastUpdatedDesc(bin)
                .orElseThrow(() -> new ResourceNotFoundException("No usage model found for bin"));
        
        double currentFill = latestRecord.getFillPercentage();
        double remainingCapacity = 100.0 - currentFill;
        
        LocalDateTime now = LocalDateTime.now();
        boolean isWeekend = now.getDayOfWeek().getValue() >= 6;
        double dailyIncrease = isWeekend ? model.getAvgDailyIncreaseWeekend() : model.getAvgDailyIncreaseWeekday();
        
        int daysUntilFull = dailyIncrease > 0 ? (int) Math.ceil(remainingCapacity / dailyIncrease) : Integer.MAX_VALUE;
        if (daysUntilFull < 0) daysUntilFull = 0;
        
        LocalDate predictedFullDate = LocalDate.now().plusDays(daysUntilFull);
        
        if (predictedFullDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Predicted full date cannot be before current date");
        }
        
        OverflowPrediction prediction = new OverflowPrediction(bin, predictedFullDate, daysUntilFull, model, now);
        return predictionRepository.save(prediction);
    }

    @Override
    public OverflowPrediction getPredictionById(Long id) {
        return predictionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prediction not found"));
    }

    @Override
    public List<OverflowPrediction> getPredictionsForBin(Long binId) {
        Bin bin = binRepository.findById(binId)
                .orElseThrow(() -> new ResourceNotFoundException("Bin not found"));
        return predictionRepository.findAll().stream()
                .filter(p -> p.getBin().getId().equals(binId))
                .toList();
    }

    @Override
    public List<OverflowPrediction> getLatestPredictionsForZone(Long zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        return predictionRepository.findLatestPredictionsForZone(zone);
    }
}