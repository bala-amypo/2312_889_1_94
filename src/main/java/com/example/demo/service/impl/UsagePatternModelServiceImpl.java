package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Bin;
import com.example.demo.model.UsagePatternModel;
import com.example.demo.repository.BinRepository;
import com.example.demo.repository.UsagePatternModelRepository;
import com.example.demo.service.UsagePatternModelService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsagePatternModelServiceImpl implements UsagePatternModelService {
    private final UsagePatternModelRepository modelRepository;
    private final BinRepository binRepository;

    public UsagePatternModelServiceImpl(UsagePatternModelRepository modelRepository, BinRepository binRepository) {
        this.modelRepository = modelRepository;
        this.binRepository = binRepository;
    }

    @Override
    public UsagePatternModel createModel(UsagePatternModel model) {
        if (model.getBin() != null && model.getBin().getId() != null) {
            Bin bin = binRepository.findById(model.getBin().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bin not found"));
            model.setBin(bin);
        }
        
        if (model.getAvgDailyIncreaseWeekday() == null || model.getAvgDailyIncreaseWeekday() < 0) {
            throw new BadRequestException("Average daily increase weekday must be non-negative");
        }
        
        if (model.getAvgDailyIncreaseWeekend() == null || model.getAvgDailyIncreaseWeekend() < 0) {
            throw new BadRequestException("Average daily increase weekend must be non-negative");
        }
        
        return modelRepository.save(model);
    }

    @Override
    public UsagePatternModel updateModel(Long id, UsagePatternModel model) {
        UsagePatternModel existing = modelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Model not found"));
        
        if (model.getAvgDailyIncreaseWeekday() != null) {
            if (model.getAvgDailyIncreaseWeekday() < 0) {
                throw new BadRequestException("Average daily increase weekday must be non-negative");
            }
            existing.setAvgDailyIncreaseWeekday(model.getAvgDailyIncreaseWeekday());
        }
        
        if (model.getAvgDailyIncreaseWeekend() != null) {
            if (model.getAvgDailyIncreaseWeekend() < 0) {
                throw new BadRequestException("Average daily increase weekend must be non-negative");
            }
            existing.setAvgDailyIncreaseWeekend(model.getAvgDailyIncreaseWeekend());
        }
        
        if (model.getLastUpdated() != null) {
            existing.setLastUpdated(model.getLastUpdated());
        }
        
        return modelRepository.save(existing);
    }

    @Override
    public UsagePatternModel getModelForBin(Long binId) {
        Bin bin = binRepository.findById(binId)
                .orElseThrow(() -> new ResourceNotFoundException("Bin not found"));
        return modelRepository.findTop1ByBinOrderByLastUpdatedDesc(bin)
                .orElseThrow(() -> new ResourceNotFoundException("Model not found for bin"));
    }

    @Override
    public List<UsagePatternModel> getAllModels() {
        return modelRepository.findAll();
    }
}