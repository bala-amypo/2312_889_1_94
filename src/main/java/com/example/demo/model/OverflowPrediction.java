package com.example.demo.model;

import java.time.Instant;
import java.util.Date;

public class OverflowPrediction {

    private Long id;
    private Bin bin;
    private Date predictedFullDate;
    private Integer daysUntilFull;
    private UsagePatternModel modelUsed;
    private Instant generatedAt;

    public OverflowPrediction() {}

    public OverflowPrediction(Long id, Bin bin, Date predictedFullDate, Integer daysUntilFull,
                              UsagePatternModel modelUsed, Instant generatedAt) {
        this.id = id;
        this.bin = bin;
        this.predictedFullDate = predictedFullDate;
        this.daysUntilFull = daysUntilFull;
        this.modelUsed = modelUsed;
        this.generatedAt = generatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public Date getPredictedFullDate() {
        return predictedFullDate;
    }

    public void setPredictedFullDate(Date predictedFullDate) {
        this.predictedFullDate = predictedFullDate;
    }

    public Integer getDaysUntilFull() {
        return daysUntilFull;
    }

    public void setDaysUntilFull(Integer daysUntilFull) {
        this.daysUntilFull = daysUntilFull;
    }

    public UsagePatternModel getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(UsagePatternModel modelUsed) {
        this.modelUsed = modelUsed;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
