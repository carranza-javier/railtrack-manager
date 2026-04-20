package com.railtrack.railtrack_backend.dto;

import com.railtrack.railtrack_backend.model.enums.TrackStatus;
import com.railtrack.railtrack_backend.model.enums.TrackType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TrackSegmentResponse {

    private Long id;
    private String name;
    private String lineCode;
    private TrackType trackType;
    private TrackStatus status;
    private Double length;
    private LocalDate lastMaintenanceDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
