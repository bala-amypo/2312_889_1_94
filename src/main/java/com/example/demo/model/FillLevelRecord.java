package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fill_level_records")
public class FillLevelRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bin_id")
    private Bin bin;

    private Double fillPercentage;

    private LocalDateTime recordedAt;

    private Boolean isWeekend;

    public FillLevelRecord() {}

    public FillLevelRecord(Bin bin, Double fillPercentage, LocalDateTime recordedAt, Boolean isWeekend) {
        this.bin = bin;
        this.fillPercentage = fillPercentage;
        this.recordedAt = recordedAt;
        this.isWeekend = isWeekend;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Bin getBin() { return bin; }
    public void setBin(Bin bin) { this.bin = bin; }

    public Double getFillPercentage() { return fillPercentage; }
    public void setFillPercentage(Double fillPercentage) { this.fillPercentage = fillPercentage; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

    public Boolean getIsWeekend() { return isWeekend; }
    public void setIsWeekend(Boolean isWeekend) { this.isWeekend = isWeekend; }
}